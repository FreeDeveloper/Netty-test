package com.smile.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

/**
 * 一个简单的tcp协议的回馈服务器
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-26 9:42 下午
 */
public class TCPEchoClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        String serverAddress = "127.0.0.1";
        byte[] data = "Hello Server.this is a good client.I want say it's a nice day!".getBytes();
        int serverPort = 8888;

        // 创建Socket
        Socket socket = new Socket();
        // 连接到目标，三次握手
        socket.connect(new InetSocketAddress(serverAddress,serverPort));
        System.out.println("Connected to server...sending echo message");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Send the encoded String to the server
        out.write(data);

        // Receive the same string back from the server
        // Total bytes received so far
        int totalBytesReceived = 0;
        // Byte received in last read
        int bytesReceived;

        byte[] byteBuf = new byte[data.length];

        while (totalBytesReceived < data.length) {
            bytesReceived = in.read(byteBuf, totalBytesReceived, data.length - totalBytesReceived);
            if (bytesReceived == -1) {
                throw new SocketException("connection closed prematurely");
            }
            System.out.println(byteBuf.length);
            totalBytesReceived += bytesReceived;
        }
        // data array is full
        System.out.println("Received: " + new String(data));
        socket.close();
    }
}
