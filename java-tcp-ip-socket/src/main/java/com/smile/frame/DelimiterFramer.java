package com.smile.frame;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 基于界限符的分帧方法
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 10:55 上午
 */
public class DelimiterFramer implements Framer {
    // 数据源
    private InputStream in;
    // 消息界限符
    private static final byte DELIMITER = '\n';

    @Override
    public void frameMsg(byte[] message, OutputStream out) throws IOException {
        for (byte b : message) {
            if (b == DELIMITER) {
                throw new IOException("消息包含界限符");
            }
        }
        out.write(message);
        out.write(DELIMITER);
        out.flush();
    }

    @Override
    public byte[] nextMsg() throws IOException {
        ByteArrayOutputStream messageBuf = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = in.read()) != DELIMITER) {
            // 没有结束
            if (nextByte == -1) {
                // 如果没有读到byte
                if (messageBuf.size() == 0) {
                    return null;
                } else {
                    throw new EOFException("Non-empting message without delimiter");
                }
            }
            // 写入缓冲区
            messageBuf.write(nextByte);
        }
        return messageBuf.toByteArray();
    }
}
