package com.smile.order.server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.smile.order.server.codec.OrderFrameDecoder;
import com.smile.order.server.codec.OrderFrameEncoder;
import com.smile.order.server.codec.OrderProtocolDecoder;
import com.smile.order.server.codec.OrderProtocolEncoder;
import com.smile.order.server.handler.MetricsHandler;
import com.smile.order.server.handler.OrderServerProcessHandler;
import com.smile.order.server.metric.MyEventLoopMetricsThread;
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
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * 设置参数并增加metric
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:21 下午
 */
public class AdvanceOrderServer {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置channel
        serverBootstrap.channel(NioServerSocketChannel.class);
        // 使用自定义ThreadFactory设置线程名
        NioEventLoopGroup boss = new NioEventLoopGroup(new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(new DefaultThreadFactory("worker"));
        // 设置EventLoopGroup
        serverBootstrap.group(boss, worker);
        // 设置日志级别
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        // 统计eventLoop的状态
        ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
        schedule.scheduleAtFixedRate(new MyEventLoopMetricsThread(worker), 0, 5, TimeUnit.SECONDS);

        // 设置系统参数
        // 设置TCP_NODELAY=true，表示关闭Nagle算法
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        // 设置客户端连接请求的等待队列大小
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        MetricsHandler metricsHandler = new MetricsHandler();

        // 设置childchannel
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                // 打印底层的日志
                pipeline.addLast("lowLoggingHandler", new LoggingHandler(LogLevel.DEBUG));
                // 设置handler的名字
                pipeline.addLast("orderFrameDecoder", new OrderFrameDecoder());
                pipeline.addLast("orderFrameEncoder", new OrderFrameEncoder());
                pipeline.addLast("orderProtocolEncoder", new OrderProtocolEncoder());
                pipeline.addLast("orderProtocolDecoder", new OrderProtocolDecoder());

                pipeline.addLast("metricsHandle", metricsHandler);
                pipeline.addLast("highLoggingHandler", new LoggingHandler(LogLevel.INFO));

                pipeline.addLast("orderServerProcessHandler", new OrderServerProcessHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();
    }
}
