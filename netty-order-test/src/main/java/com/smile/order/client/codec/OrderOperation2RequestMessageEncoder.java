package com.smile.order.client.codec;

import java.util.List;

import com.smile.order.common.Operation;
import com.smile.order.common.RequestMessage;
import com.smile.order.utils.IdUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:58 下午
 */
public class OrderOperation2RequestMessageEncoder extends MessageToMessageEncoder<Operation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage(IdUtils.nextId(), operation);

        out.add(requestMessage);
    }
}
