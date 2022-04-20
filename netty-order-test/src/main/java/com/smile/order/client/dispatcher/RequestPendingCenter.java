package com.smile.order.client.dispatcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.smile.order.common.OperationResult;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 6:19 下午
 */
public class RequestPendingCenter {
    private Map<Long, OperationResultFuture> pendingRequests = new ConcurrentHashMap<>();

    public void add(Long messageId, OperationResultFuture future) {
        this.pendingRequests.put(messageId, future);
    }

    public void set(Long messageId, OperationResult result) {
        OperationResultFuture operationResultFuture = pendingRequests.get(messageId);

        if(operationResultFuture != null) {
            operationResultFuture.setSuccess(result);
            pendingRequests.remove(messageId);
        }
    }
}
