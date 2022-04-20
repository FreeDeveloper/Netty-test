package com.smile.order.auth;

import com.smile.order.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:36 下午
 */
@Data
@AllArgsConstructor
public class AuthOperationResult extends OperationResult {
    private boolean passAuth;
}
