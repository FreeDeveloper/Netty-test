package com.smile.order.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 自定义一次解码器，用于请求接收方将byte转换为ByteBuf
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:04 下午
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
