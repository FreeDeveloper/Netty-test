package com.smile.order.order;

import java.util.concurrent.TimeUnit;

import com.smile.order.common.Operation;
import com.smile.order.common.OperationResult;
import com.google.common.util.concurrent.Uninterruptibles;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:35 下午
 */
@Data
@Slf4j
public class OrderOperation extends Operation {
    private int bookId;
    private String bookName;

    public OrderOperation(int bookId, String bookName) {
        this.bookId = bookId;
        this.bookName = bookName;
    }

    public OperationResult execute() {
        log.info("order's executing startup with orderRequest: " + toString());
        //execute order logic
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        log.info("order's executing complete");
        OrderOperationResult orderResponse = new OrderOperationResult(bookId, bookName, true);
        return orderResponse;
    }
}
