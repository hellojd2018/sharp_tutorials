package com.baeldung.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * sample client 调用远程服务
 */
class RemoteServiceTestCommand extends HystrixCommand<String> {

    private final RemoteServiceTestSimulator remoteService;

    RemoteServiceTestCommand(Setter config, RemoteServiceTestSimulator remoteService) {
        super(config);
        this.remoteService = remoteService;
    }
//调用过程被隔离
    @Override
    protected String run() throws Exception {
        return remoteService.execute();
    }
}