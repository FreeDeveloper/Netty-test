package com.smile.order.server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.smile.order.server.codec.OrderFrameDecoder;
import com.smile.order.server.codec.OrderFrameEncoder;
import com.smile.order.server.codec.OrderProtocolDecoder;
import com.smile.order.server.codec.OrderProtocolEncoder;
import com.smile.order.server.handler.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 服务端主服务
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:21 下午
 */
public class OrderServer {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置channel
        serverBootstrap.channel(NioServerSocketChannel.class);
        // 设置EventLoopGroup
        serverBootstrap.group(new NioEventLoopGroup());
        // 设置日志级别
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        // 设置系统参数
        // 设置TCP_NODELAY=true，表示关闭Nagle算法
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY,true);
        // 设置客户端连接请求的等待队列大小
        serverBootstrap.option(NioChannelOption.SO_BACKLOG,1024);

        // 设置childchannel
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                pipeline.addLast(new OrderServerProcessHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();
    }
}
