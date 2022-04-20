package com.smile.websocket.handler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.smile.websocket.PushSubscription;
import com.smile.websocket.common.OperationResult;
import com.smile.websocket.common.RequestMessage;
import com.smile.websocket.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-17 3:22 下午
 */
public class PushWebSocketHandler extends SimpleChannelInboundHandler<RequestMessage> {
    // 记录链接关系
    private final ConcurrentMap<String, Set<PushSubscription>> pushSubscriptions = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        // 这个是自定义的日志工具类，可见其它文章
        System.out.println("收到的文本消息：" + requestMessage);

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setUserId(requestMessage.getUserId());
        responseMessage.setMessageBody(new OperationResult());

        // 写回客户端，这里是广播
        ctx.writeAndFlush(responseMessage);
    }


}
