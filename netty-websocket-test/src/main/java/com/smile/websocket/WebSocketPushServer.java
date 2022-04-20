/*
 * Copyright 2020 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.smile.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

public class WebSocketPushServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));

    public void start(final int port) throws Exception {
        // 使用自定义ThreadFactory设置线程名
        NioEventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(new DefaultThreadFactory("worker"));
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketPushServerInitializer("/push"));

            // 设置日志级别
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            // 设置TCP_NODELAY=true，表示关闭Nagle算法
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
            // 设置客户端连接请求的等待队列大小
            bootstrap.option(NioChannelOption.SO_BACKLOG, 1024);
            bootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("Open your web browser and navigate to http://127.0.0.1:" + PORT + '/');
                } else {
                    System.out.println("Cannot start server, follows exception " + future.cause());
                }
            }).channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new WebSocketPushServer().start(PORT);
    }
}
