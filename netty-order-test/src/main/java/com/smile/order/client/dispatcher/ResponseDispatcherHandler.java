package com.smile.order.client.dispatcher;

import com.smile.order.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 6:22 下午
 */
public class ResponseDispatcherHandler extends SimpleChannelInboundHandler<ResponseMessage> {
    private RequestPendingCenter requestPendingCenter;

    public ResponseDispatcherHandler(RequestPendingCenter requestPendingCenter) {
        this.requestPendingCenter = requestPendingCenter;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseMessage responseMessage) throws Exception {
        long messageId = responseMessage.getMessageHeader().getMessageId();
        requestPendingCenter.set(messageId, responseMessage.getMessageBody());
    }
}
