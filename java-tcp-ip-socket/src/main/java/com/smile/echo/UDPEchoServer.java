package com.smile.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-26 10:40 下午
 */
public class UDPEchoServer {
    // Maximum size od echo datagram
    // 缓冲区的大小应该超过数据报文的大小，否则只能接收到一部分数据
    private static final int ECHO_MAX = 100;

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8888);
        DatagramPacket packet = new DatagramPacket(new byte[ECHO_MAX], ECHO_MAX);

        // Run forever.receiving and echoing datagram
        while (true) {
            // 从UDP套接字消息队列中取一条消息。这个消息是完整的一条报文，不同于tcp的流式传输
            // 由于DatagramPacket实力能够传输的最大数据量是65507字节，即大概不到64k
            // 所以我们的缓冲区设置成65600是比较保险的操作
            socket.receive(packet);
            System.out.println("Handling client at" + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
            System.out.println(new String(packet.getData()));
            //Send the same packet to client
            socket.send(packet);
            //Reset length to avoid shrinking buffer
            packet.setLength(ECHO_MAX);
        }
    }
}
