package com.smile.websocket.keepalive;


import com.smile.websocket.common.Operation;
import com.smile.websocket.common.OperationResult;

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
