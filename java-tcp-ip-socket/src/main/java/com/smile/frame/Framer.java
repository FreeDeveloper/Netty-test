package com.smile.frame;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 将流数据组合成帧的类
 * UDP数据包是将消息负载在一个DatagramPacket中，每一个DatagramPacket都是一个完整的消息，接受者能准确的知道消息的长度
 * TCP发送没有消息边界的概念，因此有粘包和半包问题
 * 基于定界符，消息的结束由一个唯一的标记之处，特殊标记不能在数据中出现
 * 基于显示长度，在变长字段或消息前附加一个固定大小的字段，用来标识消息的长度，长度小于256，需要1个字节，小于65536，需要2个字节
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 10:50 上午
 */
public interface Framer {
    void frameMsg(byte[] message, OutputStream outputStream) throws IOException;

    byte[] nextMsg() throws IOException;
}
