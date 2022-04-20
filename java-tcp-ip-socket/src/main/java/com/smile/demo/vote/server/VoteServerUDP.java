package com.smile.demo.vote.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.VoteService;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.demo.vote.codec.VoteMsgTextCodec;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 12:15 下午
 */
public class VoteServerUDP {
    public static void main(String[] args) throws IOException {
        int serverPort = 8888;

        // 启动UDP端口
        DatagramSocket socket = new DatagramSocket(serverPort);
        DatagramPacket message = new DatagramPacket(new byte[VoteMsgTextCodec.MAX_WIRE_LENGTH], VoteMsgTextCodec.MAX_WIRE_LENGTH);
        VoteMsgCodec codec = new VoteMsgTextCodec();
        VoteService voteService = new VoteService();
        byte[] encodeVote;

        while(true){
            // 处理请求
            socket.receive(message);
            encodeVote = Arrays.copyOfRange(message.getData(),0,message.getLength());
            System.out.println("Received Text-Encoded Request (" + encodeVote.length + " bytes)");
            VoteMsg vote = codec.fromWire(encodeVote);
            System.out.println(vote);

            // 返回响应
            VoteMsg responseMsg = voteService.handleRequest(vote);
            encodeVote = codec.toWire(responseMsg);
            DatagramPacket responseMessage = new DatagramPacket(encodeVote, encodeVote.length,message.getAddress(),message.getPort());
            socket.send(responseMessage);
        }
    }
}
