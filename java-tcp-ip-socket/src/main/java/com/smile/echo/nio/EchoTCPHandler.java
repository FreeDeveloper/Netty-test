package com.smile.echo.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-04-09 9:44 下午
 */
public class EchoTCPHandler {
    public void handleAccept(SelectionKey key) throws IOException {
        // 注册读事件
        SocketChannel clientChannel = ((ServerSocketChannel)key.channel()).accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(),SelectionKey.OP_READ);
    }

    public void handleRead(SelectionKey key) {

    }

    public void handleWrite(SelectionKey key) {

    }
}
