package com.smile.order.server;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.smile.order.server.codec.OrderFrameDecoder;
import com.smile.order.server.codec.OrderFrameEncoder;
import com.smile.order.server.codec.OrderProtocolDecoder;
import com.smile.order.server.codec.OrderProtocolEncoder;
import com.smile.order.server.handler.MetricsHandler;
import com.smile.order.server.handler.OrderServerProcessHandler;
import com.smile.order.server.handler.ServerIdleCheckHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

/**
 * 设置参数并增加metric，使用独立的线程池来处理业务逻辑
 * 增加流量整形
 * 增加idle监测
 * 增加Ip黑名单过滤
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 5:21 下午
 */
public class AdvanceIpFilterServer {

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
//        ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
//        schedule.scheduleAtFixedRate(new MyEventLoopMetricsThread(worker), 0, 5, TimeUnit.SECONDS);

        // 使用单独的线程池处理业务逻辑
        UnorderedThreadPoolEventExecutor business = new UnorderedThreadPoolEventExecutor(10,new DefaultThreadFactory("business"));

        // 设置系统参数
        // 设置TCP_NODELAY=true，表示关闭Nagle算法
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        // 设置客户端连接请求的等待队列大小
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);

        MetricsHandler metricsHandler = new MetricsHandler();

        // 流量整形，限制读写流量100M
        GlobalTrafficShapingHandler trafficShapingHandler = new GlobalTrafficShapingHandler(Executors.newSingleThreadScheduledExecutor(),100 * 1024 * 1024,100* 1024*1024);

        // 增加IP黑名单过滤,过滤规则是拒掉127.1.*.*的所有ip
        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.1.0.1",16, IpFilterRuleType.REJECT);
        RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);
        // 设置childchannel
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("ipFilter",ruleBasedIpFilter);
                // 流量整形，可以加到最前面，限制读写流量100M
                pipeline.addLast("trafficShapeHandler", trafficShapingHandler);
                // 增加idle监测
                pipeline.addLast("idleCheck",new ServerIdleCheckHandler());
                // 打印底层的日志
                pipeline.addLast("lowLoggingHandler", new LoggingHandler(LogLevel.DEBUG));
                // 设置handler的名字
                pipeline.addLast("orderFrameDecoder", new OrderFrameDecoder());
                pipeline.addLast("orderFrameEncoder", new OrderFrameEncoder());
                pipeline.addLast("orderProtocolEncoder", new OrderProtocolEncoder());
                pipeline.addLast("orderProtocolDecoder", new OrderProtocolDecoder());

                pipeline.addLast("metricsHandle", metricsHandler);
                pipeline.addLast("highLoggingHandler", new LoggingHandler(LogLevel.INFO));

                // 写增强，不必每次都flush，只有到达特定次数才flush,参数表示5次flush，且支持业务异步线程
                pipeline.addLast("flushEnhance",new FlushConsolidationHandler(5,true));
                pipeline.addLast(business,"orderServerProcessHandler", new OrderServerProcessHandler());
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

        channelFuture.channel().closeFuture().get();
    }
}
