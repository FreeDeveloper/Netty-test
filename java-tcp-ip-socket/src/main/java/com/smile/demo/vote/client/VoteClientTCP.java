package com.smile.demo.vote.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.smile.demo.vote.VoteMsg;
import com.smile.demo.vote.codec.VoteMsgBinCodec;
import com.smile.demo.vote.codec.VoteMsgCodec;
import com.smile.frame.Framer;
import com.smile.frame.LengthFramer;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:50 上午
 */
public class VoteClientTCP {
    public static final int CANDIDATE_ID = 888;

    public static void main(String[] args) throws IOException {
        String destAddr = "";
        int destPort = 8888;

        Socket socket = new Socket(destAddr, destPort);
        OutputStream out = socket.getOutputStream();

        // 创建编解码器 和 frame处理器
        VoteMsgCodec codec = new VoteMsgBinCodec();
        Framer framer = new LengthFramer(socket.getInputStream());

        // 创建发送请求
        VoteMsg msg = new VoteMsg(true, false, CANDIDATE_ID, 0);
        byte[] encodedMsg = codec.toWire(msg);

        // 发送请求
        System.out.println("Sending Inquiry(" + encodedMsg.length + "bytes):");
        System.out.println(msg);
        framer.frameMsg(encodedMsg, out);

        //发送投票信息
        msg.setInquiry(false);
        encodedMsg = codec.toWire(msg);
        System.out.println("Sending vote(" + encodedMsg.length + "bytes):");
        framer.frameMsg(encodedMsg, out);

        // 接收相应
        encodedMsg = framer.nextMsg();
        msg = codec.fromWire(encodedMsg);
        System.out.println("Received Response(" + encodedMsg.length + "bytes):");
        System.out.println(msg);

        msg = codec.fromWire(framer.nextMsg());
        System.out.println("Received Response(" + encodedMsg.length + "bytes):");
        System.out.println(msg);

        socket.close();
    }
}
