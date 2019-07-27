package com.baeldung.hystrix;

/**
 * 代表远程一个服务
 * 模拟了远程系统中耗时的过程，导致对呼叫服务的响应延迟：
 */
public class RemoteServiceTestSimulator {

    private long wait;

    RemoteServiceTestSimulator(long wait) throws InterruptedException {
        this.wait = wait;
    }

    String execute() throws InterruptedException {
        Thread.sleep(wait);
        return "Success";
    }
}