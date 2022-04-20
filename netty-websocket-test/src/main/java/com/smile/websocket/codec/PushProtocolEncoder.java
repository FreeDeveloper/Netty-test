package com.smile.websocket.codec;

import java.util.List;

import com.smile.websocket.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 自定义的二次编码器，用于将服务端响应转换为TextWebSocketFrame
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:06 下午
 */
public class PushProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> out) throws Exception {
        out.add(new TextWebSocketFrame("服务端返回数据：" + responseMessage.getMessageBody()));
    }
}
