package com.smile.echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 一个udp的客户端
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-26 10:26 下午
 */
public class UDPEchoClientTimeout {
    // Resend timeout(milliseconds)
    private static final int TIMEOUT = 3000;
    // Maximum retransmissions
    private static final int MAX_TRIES = 5;

    public static void main(String[] args) throws IOException {
        // 要发送的数据
        byte[] message = "Hello Server.this is a good client.I want say it's a nice day!".getBytes();
        DatagramSocket socket = new DatagramSocket(12345);

        // 创建一个发送报文
        DatagramPacket sendPacket = new DatagramPacket(message, message.length, new InetSocketAddress("127.0.0.1", 8888));
        // 创建一个接收报文
        DatagramPacket receivePacket = new DatagramPacket(new byte[message.length], message.length);
        // 发送消息
        socket.send(sendPacket);
        socket.receive(receivePacket);

        System.out.println("Received: " + new String(receivePacket.getData()));
        socket.close();
    }
}
