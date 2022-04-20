package com.smile.order.keepalive;

import com.smile.order.common.Operation;
import com.smile.order.common.OperationResult;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:35 下午
 */
public class KeepAliveOperation extends Operation {
    private long time;

    public KeepAliveOperation() {
        this.time = System.currentTimeMillis();
    }

    public OperationResult execute() {
        return new KeepAliveOperationResult(time);
    }
}
