package com.smile.websocket.common;

/**
 * netty自定义协议传输对象
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:10 下午
 */
public abstract class Message<T extends MessageBody> {
    // 基本信息
    private String userId;
    private String deviceId;
    private String clientInfo;
    private int opCode;

    T messageBody;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public T getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(T messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * 根据opType获取message的类
     */
    public abstract Class<T> getMessageBodyByDecodeClass(int opCode);
}
