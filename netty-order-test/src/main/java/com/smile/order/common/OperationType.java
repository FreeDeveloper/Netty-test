package com.smile.order.common;

import java.util.function.Predicate;

import com.smile.order.auth.AuthOperation;
import com.smile.order.auth.AuthOperationResult;
import com.smile.order.keepalive.KeepAliveOperation;
import com.smile.order.keepalive.KeepAliveOperationResult;
import com.smile.order.order.OrderOperation;
import com.smile.order.order.OrderOperationResult;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:33 下午
 */
public enum OperationType {
    // 鉴权
    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    // 心跳监测
    KEEP_ALIVE(2, KeepAliveOperation.class, KeepAliveOperationResult.class),
    // 下单
    ORDER(3, OrderOperation.class, OrderOperationResult.class);

    private int opCode;
    private Class<? extends Operation> operationClazz;
    private Class<? extends OperationResult> operationResultClazz;

    OperationType(int opCode, Class<? extends Operation> operationClazz, Class<? extends OperationResult> operationResultClazz) {
        this.opCode = opCode;
        this.operationClazz = operationClazz;
        this.operationResultClazz = operationResultClazz;
    }

    public int getOpCode() {
        return opCode;
    }

    public Class<? extends Operation> getOperationClazz() {
        return operationClazz;
    }

    public Class<? extends OperationResult> getOperationResultClazz() {
        return operationResultClazz;
    }

    public static OperationType fromOpCode(int type) {
        return getOperationType(requestType -> requestType.opCode == type);
    }

    public static OperationType fromOperation(Operation operation) {
        return getOperationType(requestType -> requestType.operationClazz == operation.getClass());
    }

    private static OperationType getOperationType(Predicate<OperationType> predicate) {
        OperationType[] values = values();
        for (OperationType operationType : values) {
            if (predicate.test(operationType)) {
                return operationType;
            }
        }

        throw new AssertionError("no found type");
    }
}
