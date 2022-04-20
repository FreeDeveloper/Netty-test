package com.smile.order.client;

import java.util.concurrent.ExecutionException;

import com.smile.order.client.codec.OrderFrameDecoder;
import com.smile.order.client.codec.OrderFrameEncoder;
import com.smile.order.client.codec.OrderOperation2RequestMessageEncoder;
import com.smile.order.client.codec.OrderProtocolDecoder;
import com.smile.order.client.codec.OrderProtocolEncoder;
import com.smile.order.order.OrderOperation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:57 下午
 */
public class OrderClientV2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        // 设置channel
        bootstrap.channel(NioSocketChannel.class);
        // 设置EventLoopGroup
        bootstrap.group(new NioEventLoopGroup());
        // 设置NIO的链接超时时间，默认30S
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS,10 * 1000);

        // 设置childChannel
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderOperation2RequestMessageEncoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
        channelFuture.sync();

        OrderOperation orderOperation = new OrderOperation(1, "西红柿");
        channelFuture.channel().writeAndFlush(orderOperation);

        channelFuture.channel().closeFuture().get();
    }
}
