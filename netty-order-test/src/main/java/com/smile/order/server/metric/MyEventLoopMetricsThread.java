package com.smile.order.server.metric;

import java.util.Iterator;

import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-19 10:47 下午
 */
@Slf4j
public class MyEventLoopMetricsThread implements Runnable {
    private NioEventLoopGroup eventLoopGroup;

    public MyEventLoopMetricsThread(NioEventLoopGroup eventLoopGroup) {
        this.eventLoopGroup = eventLoopGroup;
    }


    @Override
    public void run() {
        log.info("executorCount:{}", eventLoopGroup.executorCount());
        Iterator<EventExecutor> it = eventLoopGroup.iterator();
        while (it.hasNext()) {
            NioEventLoop eventLoop = (NioEventLoop) it.next();
            log.info(eventLoop.toString() + " pendingTask:{}", eventLoop.pendingTasks());
        }

    }
}
