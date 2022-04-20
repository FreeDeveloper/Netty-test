package com.smile.demo.vote.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.demo.vote.codec.VoteMsgTextCodec;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 12:08 下午
 */
public class VoteClientUDP {

    public static void main(String[] args) throws IOException {
        InetAddress destAddr = InetAddress.getByName("127.0.0.1");
        int destPort = 8888;
        int candidateID = 888;

        DatagramSocket socket = new DatagramSocket();
        socket.connect(destAddr, destPort);

        VoteMsg vote = new VoteMsg(false, false, candidateID, 0);

        VoteMsgCodec codec = new VoteMsgTextCodec();
        byte[] encodeVote = codec.toWire(vote);
        System.out.println("Sending Text-Encoded Request (" + encodeVote.length + " bytes)");
        System.out.println(vote);

        DatagramPacket message = new DatagramPacket(encodeVote, encodeVote.length);
        socket.send(message);

        message = new DatagramPacket(new byte[VoteMsgTextCodec.MAX_WIRE_LENGTH], VoteMsgTextCodec.MAX_WIRE_LENGTH);
        socket.receive(message);
        encodeVote = Arrays.copyOfRange(message.getData(), 0, message.getLength());
        System.out.println("Received Text-Encoded Request (" + encodeVote.length + " bytes)");
        vote = codec.fromWire(encodeVote);
        System.out.println(vote);
    }

}
