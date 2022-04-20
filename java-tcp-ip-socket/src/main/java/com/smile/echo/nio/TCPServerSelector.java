package com.smile.echo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-04-09 9:41 下午
 */
public class TCPServerSelector {
    public static void main(String[] args) throws IOException {
        // 创建selector
        Selector selector = Selector.open();

        // 启动ServerSocketChannel绑定8888端口，注册Accept事件
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        EchoTCPHandler tcpHandler = new EchoTCPHandler();
        while (true) {
            if (selector.select() > 0) {
                Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
                while (keyIter.hasNext()) {
                    SelectionKey key = keyIter.next();
                    // 处理accept事件
                    if (key.isAcceptable()) {
                        tcpHandler.handleAccept(key);
                    }
                    // 处理读事件
                    if (key.isReadable()) {
                        tcpHandler.handleRead(key);
                    }
                    // 处理写事件
                    if (key.isValid() && key.isWritable()) {
                        tcpHandler.handleWrite(key);
                    }

                    keyIter.remove();
                }
            }
        }
    }
}
