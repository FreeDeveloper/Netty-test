package com.smile.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-26 10:03 下午
 */
public class TCPEchoServer {
    // Size of receive buffer
    private static final int BUF_SIZE = 32;

    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        // 创建Socket
        ServerSocket serverSocket = new ServerSocket();
        // bind到指定的IP和端口，这里我绑定所有的IP，在bind方法里执行了绑定和监听两个动作
        serverSocket.bind(new InetSocketAddress(serverPort));

        // Size of received message
        int receivedMessageSize;
        // Receive buffer
        byte[] receiveBuf = new byte[BUF_SIZE];
        // Run forever,accepting and servicing connection
        while (true) {
            // get client connection
            Socket clientSocket = serverSocket.accept();
            SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
            System.out.println("Handing client at " + clientAddress);

            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            // Receive until client closes connection,indicated by -1 return
            while ((receivedMessageSize = in.read(receiveBuf)) != -1) {
                out.write(receiveBuf, 0, receivedMessageSize);
            }
            // Close the socket.We are done with this client
            clientSocket.close();
        }
    }
}
