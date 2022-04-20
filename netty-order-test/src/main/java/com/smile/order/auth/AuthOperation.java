package com.smile.order.auth;

import com.smile.order.common.Operation;
import com.smile.order.common.OperationResult;
import lombok.Data;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:35 下午
 */
@Data
public class AuthOperation extends Operation {
    private String userName;
    private String password;

    public AuthOperation(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public AuthOperationResult execute() {
        if ("admin".equals(userName)) {
            return new AuthOperationResult(true);
        }
        return new AuthOperationResult(false);
    }
}
