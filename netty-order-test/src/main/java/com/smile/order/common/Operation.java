package com.smile.order.common;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:31 下午
 */
public abstract class Operation extends MessageBody{
    public abstract OperationResult execute();
}
