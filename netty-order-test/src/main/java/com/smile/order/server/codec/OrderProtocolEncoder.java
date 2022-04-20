package com.smile.order.server.codec;

import java.util.List;

import com.smile.order.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 自定义的二次编码器，用于将服务端响应转换为ByteBuf
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:06 下午
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        responseMessage.encode(byteBuf);

        out.add(byteBuf);
    }
}
