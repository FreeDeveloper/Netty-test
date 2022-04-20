package com.smile.websocket.codec;

import java.util.List;

import com.smile.websocket.common.RequestMessage;
import com.smile.websocket.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * 自定义的二次解码器，用于将服务端请求从TextWebSocketFrame解码为自定义的协议
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:06 下午
 */
public class PushProtocolDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
        RequestMessage requestMessage = JsonUtils.fromJson(msg.text(),RequestMessage.class);
        out.add(requestMessage);
    }
}
