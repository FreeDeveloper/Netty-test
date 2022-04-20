package com.smile.order.common;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:53 下午
 */
public class ResponseMessage extends Message<OperationResult> {
    @Override
    public Class getMessageBodyByDecodeClass(int opCode) {
        return OperationType.fromOpCode(opCode).getOperationResultClazz();
    }
}
