package com.smile.websocket.common;

import lombok.Data;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:40 下午
 */
@Data
public class RequestMessage extends Message<Operation> {

    public Class getMessageBodyByDecodeClass(int opCode) {
        return OperationType.fromOpCode(opCode).getOperationClazz();
    }
}
