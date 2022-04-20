package com.smile.demo.vote.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.VoteService;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.demo.vote.codec.VoteMsgTextCodec;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 11:11 上午
 */
public class VoteMulticastReceiver {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;
        // 多播地址
        InetAddress address = InetAddress.getByName("");

        // 启动UDP端口
        MulticastSocket socket = new MulticastSocket(serverPort);
        socket.joinGroup(address);

        DatagramPacket message = new DatagramPacket(new byte[VoteMsgTextCodec.MAX_WIRE_LENGTH], VoteMsgTextCodec.MAX_WIRE_LENGTH);
        VoteMsgCodec codec = new VoteMsgTextCodec();
        byte[] encodeVote;

        // 处理请求
        socket.receive(message);
        encodeVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());
        System.out.println("Received Text-Encoded Request (" + encodeVote.length + " bytes)");
        VoteMsg vote = codec.fromWire(encodeVote);
        System.out.println(vote);
    }
}
