package com.smile.order.server.handler;

import com.smile.order.common.Operation;
import com.smile.order.common.OperationResult;
import com.smile.order.common.RequestMessage;
import com.smile.order.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 请求处理器
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:08 下午
 */
@Slf4j
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        // 创建一个对象不释放，此时可能发生内存泄漏，可以使用netty的内存泄漏检测机制
        // 指定启动参数-Dio.netty.leakDetection.level=PARANOID，表示每个对象都检测
//        ByteBuf byteBuf = ctx.alloc().buffer();
        Operation operation = requestMessage.getMessageBody();
        OperationResult result = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(result);

        // 如果我们设置了高低水位，或者流量整形的大小，可能会导致不可写，此时需要处理不可写的情况，如果不判断一直写，会OOM
        if(ctx.channel().isActive() && ctx.channel().isWritable()) {
            ctx.writeAndFlush(responseMessage);
        } else {
            // TODO 正常线上应用应考虑重试
            log.error("message dropped!!");
        }
    }
}
