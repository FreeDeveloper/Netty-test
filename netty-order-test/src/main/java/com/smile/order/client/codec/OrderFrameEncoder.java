package com.smile.order.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * 自定义一次解码器，用于请求发送方将ByteBuf编码为byte[]
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:04 下午
 */
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }
}
