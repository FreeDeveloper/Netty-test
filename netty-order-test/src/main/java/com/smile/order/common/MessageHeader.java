package com.smile.order.common;

import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * netty自定义协议传输对象头
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-13 3:12 下午
 */
@Builder
@Getter
@Setter
public class MessageHeader implements Serializable {
    private int version = 1;
    private long messageId;
    private int opCode;
}
