package com.smile.order.common;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:40 下午
 */
public class RequestMessage extends Message<Operation> {
    public Class getMessageBodyByDecodeClass(int opCode) {
        return OperationType.fromOpCode(opCode).getOperationClazz();
    }

    public RequestMessage() {
    }

    public RequestMessage(long messageId, Operation operation) {
        MessageHeader messageHeader = MessageHeader.builder()
                .messageId(messageId)
                .opCode(OperationType.fromOperation(operation).getOpCode())
                .build();

        this.messageHeader = messageHeader;
        this.messageBody = operation;
    }
}
