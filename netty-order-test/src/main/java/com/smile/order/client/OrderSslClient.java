package com.smile.order.client;

import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import com.smile.order.auth.AuthOperation;
import com.smile.order.client.codec.OrderFrameDecoder;
import com.smile.order.client.codec.OrderFrameEncoder;
import com.smile.order.client.codec.OrderProtocolDecoder;
import com.smile.order.client.codec.OrderProtocolEncoder;
import com.smile.order.client.dispatcher.ClientIdleCheckHandler;
import com.smile.order.client.dispatcher.ClientKeepAliveHandler;
import com.smile.order.common.RequestMessage;
import com.smile.order.order.OrderOperation;
import com.smile.order.utils.IdUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.internal.UnstableApi;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:47 下午
 */
@UnstableApi
public class OrderSslClient {
    public static void main(String[] args) throws InterruptedException, ExecutionException, SSLException {
        Bootstrap bootstrap = new Bootstrap();
        // 设置channel
        bootstrap.channel(NioSocketChannel.class);
        // 设置EventLoopGroup
        bootstrap.group(new NioEventLoopGroup());
        // 设置NIO的链接超时时间，默认30S
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS,10 * 1000);

        ClientKeepAliveHandler clientKeepAliveHandler = new ClientKeepAliveHandler();

        SslContext sslContext = SslContextBuilder.forClient().build();

        // 设置childchannel
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ClientIdleCheckHandler());
                // 增加SSl
                SslHandler sslHandler = sslContext.newHandler(ch.alloc());
                pipeline.addLast(sslHandler);

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(clientKeepAliveHandler);
                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();
        channelFuture.sync();
        // 先发一个鉴权消息
        RequestMessage authMessage = new RequestMessage(IdUtils.nextId(),new AuthOperation("admin","123"));
        channelFuture.channel().writeAndFlush(authMessage);

        RequestMessage requestMessage = new RequestMessage(IdUtils.nextId(), new OrderOperation(1, "西红柿"));
        channelFuture.channel().writeAndFlush(requestMessage);

        channelFuture.channel().closeFuture().get();
    }
}
