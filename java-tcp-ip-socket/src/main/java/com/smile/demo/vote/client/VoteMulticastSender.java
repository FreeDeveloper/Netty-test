package com.smile.demo.vote.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.demo.vote.codec.VoteMsgTextCodec;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-09 11:09 上午
 */
public class VoteMulticastSender {
    public static void main(String[] args) throws IOException {
        InetAddress destAddr = InetAddress.getByName("127.0.0.1");
        int destPort = 8888;
        int candidateID = 888;

        MulticastSocket socket = new MulticastSocket();
        // 设置ttl，每经过一个路由器就会-1，限制传播的最远路径
        socket.setTimeToLive(10);
        socket.connect(destAddr, destPort);

        VoteMsg vote = new VoteMsg(false, false, candidateID, 0);

        VoteMsgCodec codec = new VoteMsgTextCodec();
        byte[] encodeVote = codec.toWire(vote);
        System.out.println("Sending Text-Encoded Request (" + encodeVote.length + " bytes)");
        System.out.println(vote);

        DatagramPacket message = new DatagramPacket(encodeVote, encodeVote.length);
        socket.send(message);

        socket.close();
    }
}
