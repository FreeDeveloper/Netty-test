package com.smile.order.order;

import com.smile.order.common.OperationResult;
import lombok.Data;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:36 下午
 */
@Data
public class OrderOperationResult extends OperationResult {

    private final int bookId;
    private final String bookName;
    private final boolean complete;

}
