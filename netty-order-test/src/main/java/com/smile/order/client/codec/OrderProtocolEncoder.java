package com.smile.order.client.codec;

import java.util.List;

import com.smile.order.common.RequestMessage;
import com.smile.order.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * 自定义的二次编码器，用于客户端请求将自定义的协议转换为ByteBuf
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:06 下午
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage requestMessage, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer();
        requestMessage.encode(byteBuf);

        out.add(byteBuf);
    }
}
