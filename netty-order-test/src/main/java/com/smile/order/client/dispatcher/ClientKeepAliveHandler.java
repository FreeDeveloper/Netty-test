package com.smile.order.client.dispatcher;

import com.smile.order.common.RequestMessage;
import com.smile.order.keepalive.KeepAliveOperation;
import com.smile.order.utils.IdUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-20 6:18 下午
 */
@Slf4j
@ChannelHandler.Sharable
public class ClientKeepAliveHandler extends ChannelDuplexHandler {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.warn("write idle happened, need to send keepalive message to keep connection!");
            KeepAliveOperation keepAliveOperation = new KeepAliveOperation();
            RequestMessage requestMessage = new RequestMessage(IdUtils.nextId(),keepAliveOperation);
            ctx.writeAndFlush(requestMessage);
        }
        super.userEventTriggered(ctx, evt);
    }
}
