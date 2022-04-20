package com.smile.demo.vote.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.VoteService;
import com.smile.demo.vote.codec.VoteMsgBinCodec;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.frame.Framer;
import com.smile.frame.LengthFramer;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 12:00 下午
 */
public class VoteServerTCP {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        ServerSocket serverSocket = new ServerSocket(port);
        VoteMsgCodec codec = new VoteMsgBinCodec();
        VoteService voteService = new VoteService();

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Handing client at" + clientSocket.getRemoteSocketAddress());

            Framer framer = new LengthFramer(clientSocket.getInputStream());
            try {
                byte[] req;
                while ((req = framer.nextMsg()) != null) {
                    System.out.println("Receive message(" + req.length + " bytes)");
                    VoteMsg responseMsg = voteService.handleRequest(codec.fromWire(req));
                    framer.frameMsg(codec.toWire(responseMsg), clientSocket.getOutputStream());
                }
            } catch (IOException e) {
                System.err.println("Error handing client:" + e.getMessage());
            } finally {
                System.out.println("Closing connection");
                clientSocket.close();
            }
        }
    }
}
