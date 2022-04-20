package com.smile.order.server.handler;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-20 6:08 下午
 */
@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {
    // 创建一个idle监测handler，每10秒进行监测
    public ServerIdleCheckHandler() {
        super(10, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        // 遇到第一次idle事件时关闭连接
        if(evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT) {
            log.warn("idle check happened,so close the connection!");
            ctx.close();
            return;
        }

        super.channelIdle(ctx, evt);
    }
}
