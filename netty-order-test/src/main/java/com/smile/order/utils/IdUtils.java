package com.smile.order.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 4:04 下午
 */
public class IdUtils {
    private static final AtomicLong idx = new AtomicLong();

    public static long nextId() {
        return idx.incrementAndGet();
    }
}
