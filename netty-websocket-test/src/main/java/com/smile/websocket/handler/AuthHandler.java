package com.smile.websocket.handler;

import com.smile.websocket.auth.AuthOperation;
import com.smile.websocket.auth.AuthOperationResult;
import com.smile.websocket.common.Operation;
import com.smile.websocket.common.RequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-20 6:53 下午
 */
@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        Operation operation = msg.getMessageBody();
        try {
            if (operation instanceof AuthOperation) {
                AuthOperation authOperation = (AuthOperation) operation;
                AuthOperationResult operationResult = authOperation.execute();
                if (operationResult.isPassAuth()) {
                    log.info("auth pass");
                } else {
                    log.error("failed to auth");
                    ctx.close();
                }
            } else {
                log.error("expect first message is auth");
                ctx.close();
            }
        } catch (Exception e) {
            log.error("exception happened");
            ctx.close();
        } finally {
            ctx.pipeline().remove(this);
        }
    }
}
