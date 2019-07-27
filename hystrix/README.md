Hystrix通过隔离故障服务和防止故障蔓延来提高系统的整体弹性

[introduction-to-hystrix](https://www.baeldung.com/introduction-to-hystrix)
[hystrix-integration-with-spring-aop](https://www.baeldung.com/hystrix-integration-with-spring-aop)
[wiki](https://github.com/Netflix/Hystrix/wiki)

## 目标
- 防止任何单个依赖项用尽所有容器（例如Tomcat）用户线程。
- 降低负载并快速失败而不是排队。
- 在可行的情况下提供回退以保护用户免于失败。
- 使用隔离技术（例如隔板，泳道和断路器模式）来限制任何一个依赖项的影响。
- 通过近实时指标，监控和警报优化发现时间
- 通过Hystrix的大多数方面的配置更改的低延迟传播和对动态属性更改的支持来优化恢复时间，这允许您使用低延迟反馈循环进行实时操作修改。
- 防止整个依赖关系客户端执行中的故障，而不仅仅是网络流量。

## 实现
- 将所有对外部系统（或“依赖项”）的调用包含在HystrixCommand或HystrixObservableCommand对象中，该对象通常在单独的线程中执行（这是命令模式的一个示例）。
- 定时调用的时间超过您定义的阈值。有一个默认值，但对于大多数依赖项，您可以通过“属性”自定义设置这些超时，以便它们略高于每个依赖项的测量的第99.5百分位性能。
- 为每个依赖项维护一个小的线程池（或信号量）;如果它变满，将立即拒绝发往该依赖项的请求而不是排队。
- 衡量成功，失败（客户端引发的异常），超时和线程拒绝。
- 如果服务的错误百分比超过阈值，则手动或自动地使断路器跳闸以停止对特定服务的所有请求一段时间。
- 当请求失败时执行回退逻辑，被拒绝，超时或短路。
- 近乎实时地监控指标和配置更改。

![](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png)
1.Construct a HystrixCommand or HystrixObservableCommand Object

Execute the Command (4种方式)
 ```java
K             value   = command.execute();
Future<K>     fValue  = command.queue();
Observable<K> ohValue = command.observe();         //hot observable
Observable<K> ocValue = command.toObservable();    //cold observable
```
Is the Response Cached?
Is the Circuit Open? ( open or tripped)
Is the Thread Pool/Queue/Semaphore Full?
HystrixObservableCommand.construct() or HystrixCommand.run()
Calculate Circuit Health
Get the Fallback
Return the Successful Response


![](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/circuit-breaker-1280.png)
1.容量满足某个阈值？[HystrixCommandProperties.circuitBreakerRequestVolumeThreshold()]
2.错误百分比超过阈值错误百分比 [HystrixCommandProperties.circuitBreakerErrorThresholdPercentage()]
3. 满足1和2，断路器从CLOSED转换到OPEN
4.OPEN时，它会短路所有针对该断路器的请求
5.经过一段时间[HystrixCommandProperties.circuitBreakerSleepWindowInMilliseconds()],下一个请求是允许通过（这是HALF-OPEN状态）。
如果请求失败，则断路器在睡眠窗口期间返回到OPEN状态。如果请求成功，则断路器转换为CLOSED

> 请注意，没有办法强制潜在的线程停止工作

> Most Java HTTP client libraries do not interpret InterruptedExceptions. So make sure to correctly configure connection and read/write timeouts on the HTTP clients


## Threads & Thread Pools
Hystrix每个依赖性使用单独线程池，作为约束给定依赖项的方式。
![](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/request-example-with-latency-1280.png)

### 优点
1. 保护应用程序完全受到失控客户端库威胁
2. 将与库隔离，不会影响其他所有内容
3. 当失败的客户端再次变得健康时，线程池将清空并且应用程序立即恢复正常性能，而不是当整个Tomcat容器不堪重负时的长恢复
4. 如果客户端库配置错误，线程池的运行状况将很快证明这一点（通过增加的错误，延迟，超时，拒绝等）
5.客户端服务改变了性能特征（这通常发生在一个问题上），这反过来导致需要调整属性（增加/减少超时，更改重试等），这又通过线程池指标（错误，延迟）变得可见 ，超时，拒绝）
6.除了隔离优势之外，拥有专用线程池还提供内置并发性，可用于在同步客户端库之上构建异步外观
### 缺点
是它们增加了计算开销。每个命令执行都涉及在单独的线程上运行命令所涉及的排队，调度和上下文切换。

## 信号量
您可以使用信号量（或计数器）来限制对任何给定依赖项的并发调用数，而不是使用线程池/队列大小。 这允许Hystrix在不使用线程池的情况下卸载负载，但它不允许超时和离开。 
如果您信任客户端而您只想减载，则可以使用此方法。
### 使用场景
- 回退Fallback：当Hystrix检索回退时，它总是在调用Tomcat线程上执行此操作。
- 执行Execution：如果将属性`execution.isolation.strategy`设置为SEMAPHORE，则Hystrix将使用信号量而不是线程来限制调用该命令的并发父线程数。