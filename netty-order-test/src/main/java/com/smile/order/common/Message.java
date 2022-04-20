package com.smile.order.common;

import java.io.Serializable;

import com.smile.order.serialization.FSTSerializer;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

/**
 * netty自定义协议传输对象
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:10 下午
 */
@Getter
@Setter
public abstract class Message<T extends MessageBody> implements Serializable {
    // 消息头
    MessageHeader messageHeader;
    // 消息体
    T messageBody;
    /**
     * 编码
     */
    public void encode(ByteBuf byteBuf) {
        byteBuf.writeBytes(FSTSerializer.encoder(this));
//        // 写入消息头
//        byteBuf.writeInt(messageHeader.getVersion());
//        byteBuf.writeLong(messageHeader.getMessageId());
//        byteBuf.writeInt(messageHeader.getOpCode());
//
//        // 写入消息体
//        byteBuf.writeBytes(JsonUtils.toJson(messageBody).getBytes());
    }

    /**
     * 解码
     */
    public void decode(ByteBuf msg) {
        byte[] bytes = new byte[msg.capacity()];
        msg.readBytes(bytes);
        Message message = FSTSerializer.decoder(bytes);
        int opCode = message.messageHeader.getOpCode();
        Class<T> bodyClass = getMessageBodyByDecodeClass(opCode);
        this.messageHeader = message.getMessageHeader();
        this.messageBody = bodyClass.cast(message.getMessageBody());

        // 读取消息头
//        int version = msg.readInt();
//        long messageId = msg.readLong();
//        int opCode = msg.readInt();
//        this.messageHeader = MessageHeader.builder()
//                .version(version)
//                .messageId(messageId)
//                .opCode(opCode)
//                .build();
//
//        // 读取消息体
//        this.messageBody = JsonUtils.fromJson(msg.toString(Charset.forName("UTF-8")), bodyClass);
    }

    /**
     * 根据opType获取message的类
     */
    public abstract Class<T> getMessageBodyByDecodeClass(int opCode);
}
