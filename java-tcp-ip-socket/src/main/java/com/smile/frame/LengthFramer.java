package com.smile.frame;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基于消息长度的分帧方法
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:06 上午
 */
public class LengthFramer implements Framer {
    public static final int MAX_MESSAGE_LENGTH = 65535;
    public static final int BYTE_MASK = 0xff;
    public static final int SHORT_MASK = 0xffff;
    public static final int BYTE_SHIFT = 8;

    private DataInputStream in;

    public LengthFramer(InputStream in) throws IOException {
        this.in = new DataInputStream(in);
    }

    @Override
    public void frameMsg(byte[] message, OutputStream outputStream) throws IOException {
        if (message.length > MAX_MESSAGE_LENGTH) {
            throw new IOException("消息过长");
        }
        // 写长度前缀，分两次写入，每次一个字节
        outputStream.write((message.length >> BYTE_SHIFT) & BYTE_MASK);
        outputStream.write(message.length & BYTE_MASK);
        // 写消息
        outputStream.write(message);
        outputStream.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        int length;
        // 读取两个字节
        length = in.readUnsignedShort();
        byte[] msg = new byte[length];
        // 读取消息
        in.readFully(msg);
        return msg;
    }
}
