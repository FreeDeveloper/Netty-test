package com.smile.order.keepalive;

import com.smile.order.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:36 下午
 */
@AllArgsConstructor
@Data
public class KeepAliveOperationResult extends OperationResult {
    private long time;
}
