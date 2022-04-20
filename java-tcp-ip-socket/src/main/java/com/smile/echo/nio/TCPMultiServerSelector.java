package com.smile.echo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-04-09 9:41 下午
 */
public class TCPMultiServerSelector {
    public static void main(String[] args) throws IOException {
        // 创建selector
        Selector selector = Selector.open();

        // 启动ServerSocketChannel绑定8888端口，注册Accept事件
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 创建10个worker
        Worker [] workers = new Worker[10];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }

        while (true) {
            if (selector.select() > 0) {
                Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
                while (keyIter.hasNext()) {
                    SelectionKey key = keyIter.next();
                    // 处理accept事件
                    if (key.isAcceptable()) {
                        // 实际可以通过策略进行选择
                        Worker worker = workers[0];
                        worker.register(key);
                    }
                    keyIter.remove();
                }
            }
        }
    }

    private static class Worker extends Thread {
        private Selector selector;
        EchoTCPHandler tcpHandler = new EchoTCPHandler();

        public Worker() throws IOException {
            selector = Selector.open();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    if (selector.select() > 0) {
                        Set<SelectionKey> keys = selector.selectedKeys();
                        for (SelectionKey key : keys) {
                            if (key.isReadable()) {
                                tcpHandler.handleRead(key);
                            }
                            if (key.isWritable()) {
                                tcpHandler.handleWrite(key);
                            }
                        }
                        keys.clear();
                    }
                } catch (Throwable t) {
                }
            }
        }

        public void register(SelectionKey key) throws IOException {
            // 注册读事件
            SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(key.selector(),SelectionKey.OP_READ);
        }
    }
}
