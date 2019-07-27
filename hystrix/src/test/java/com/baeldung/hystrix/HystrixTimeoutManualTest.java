package com.baeldung.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class HystrixTimeoutManualTest {

    @Test
    public void givenInputBobAndDefaultSettings_whenCommandExecuted_thenReturnHelloBob() {
        assertThat(new CommandHelloWorld("Bob").execute(), equalTo("Hello Bob!"));
    }

    @Test
    public void givenSvcTimeoutOf100AndDefaultSettings_whenRemoteSvcExecuted_thenReturnSuccess()
      throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroup2"));

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(100)).execute(),
          equalTo("Success"));
    }

    @Test(expected = HystrixRuntimeException.class)
    public void givenSvcTimeoutOf10000AndDefaultSettings__whenRemoteSvcExecuted_thenExpectHRE() throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest3"));
        new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(10_000)).execute();
    }

    @Test
    public void givenSvcTimeoutOf5000AndExecTimeoutOf10000_whenRemoteSvcExecuted_thenReturnSuccess()
      throws InterruptedException {
//我们通过将超时设置为500毫秒来延迟服务的响应。
// 我们还将HystrixCommand的执行超时设置为10,000 ms，从而为远程服务提供足够的响应时间。
        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest4"));
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(10_000);
        config.andCommandPropertiesDefaults(commandProperties);

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
          equalTo("Success"));
    }

    @Test(expected = HystrixRuntimeException.class)
    public void givenSvcTimeoutOf15000AndExecTimeoutOf5000__whenExecuted_thenExpectHRE()
      throws InterruptedException {
        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupTest5"));
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(5_000);//设置服务超时时间
        config.andCommandPropertiesDefaults(commandProperties);
        String execute = null;
        try {
            execute=new RemoteServiceTestCommand(config,
                new RemoteServiceTestSimulator(15_000)).execute();
        }catch (Exception e){
            assertThat(e,instanceOf(HystrixRuntimeException.class));
        }
    }
//    设置服务调用的超时并不能解决与远程服务相关的所有问题。
    //有限线程池
/*  当远程服务开始响应缓慢时，典型应用程序将继续调用该远程服务。
    应用程序不知道远程服务是否健康，并且每次请求进入时都会生成新线程;CPU利用率飙升
*/
    @Test
    public void givenSvcTimeoutOf500AndExecTimeoutOf10000AndThreadPool__whenExecuted_thenReturnSuccess()
      throws InterruptedException {

        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupThreadPool"));
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter();
        commandProperties.withExecutionTimeoutInMilliseconds(10_000);
        config.andCommandPropertiesDefaults(commandProperties);
        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
          .withMaxQueueSize(10)
          .withCoreSize(3)
          .withQueueSizeRejectionThreshold(10));
        //当最大线程数达到10且任务队列大小达到10时，Hystrix将开始拒绝请求。
        //核心大小是始终在线程池中保持活动状态的线程数。
        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
          equalTo("Success"));
    }

    @Test
    public void givenCircuitBreakerSetup__whenRemoteSvcCmdExecuted_thenReturnSuccess()
      throws InterruptedException {
    //短路断路器
        HystrixCommand.Setter config = HystrixCommand
          .Setter
          .withGroupKey(HystrixCommandGroupKey.Factory.asKey("RemoteServiceGroupCircuitBreaker"));
        HystrixCommandProperties.Setter properties = HystrixCommandProperties.Setter();
        properties.withExecutionTimeoutInMilliseconds(1000);
        //这将配置断路器窗口并定义将恢复对远程服务的请求的时间间隔
        properties.withCircuitBreakerSleepWindowInMilliseconds(4000);
        properties.withExecutionIsolationStrategy(
          HystrixCommandProperties.ExecutionIsolationStrategy.THREAD);
        properties.withCircuitBreakerEnabled(true);
        //定义在考虑故障率之前所需的最小请求数
        properties.withCircuitBreakerRequestVolumeThreshold(1);

        config.andCommandPropertiesDefaults(properties);

        config.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
          .withMaxQueueSize(1)
          .withCoreSize(1)
          .withQueueSizeRejectionThreshold(1));

        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));
        assertThat(this.invokeRemoteService(config, 10_000), equalTo(null));//发生熔断
        assertThat(this.invokeRemoteService(config, 1), equalTo(null));// 快速失败并迅速恢复
        Thread.sleep(5000);//越过我们设置的睡眠窗口的限制

        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
          equalTo("Success"));
        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
          equalTo("Success"));
        assertThat(new RemoteServiceTestCommand(config, new RemoteServiceTestSimulator(500)).execute(),
          equalTo("Success"));
    }

    public String invokeRemoteService(HystrixCommand.Setter config, int timeout)
      throws InterruptedException {
        String response = null;
        try {
            response = new RemoteServiceTestCommand(config,
              new RemoteServiceTestSimulator(timeout)).execute();
        } catch (HystrixRuntimeException ex) {
            System.out.println("ex = " + ex);
        }
        return response;
    }
}