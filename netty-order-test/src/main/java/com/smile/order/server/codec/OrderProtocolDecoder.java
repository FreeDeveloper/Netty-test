package com.smile.order.server.codec;

import java.util.List;

import com.smile.order.common.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 自定义的二次解码器，用于将服务端请求从ByteBuf解码为自定义的协议
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:06 下午
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage();
        requestMessage.decode(msg);

        out.add(requestMessage);
    }
}
