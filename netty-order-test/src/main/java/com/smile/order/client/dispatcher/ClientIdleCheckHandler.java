package com.smile.order.client.dispatcher;

import java.util.concurrent.TimeUnit;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-20 6:17 下午
 */
public class ClientIdleCheckHandler extends IdleStateHandler {
    public ClientIdleCheckHandler() {
        super(0, 5, 0);
    }
}
