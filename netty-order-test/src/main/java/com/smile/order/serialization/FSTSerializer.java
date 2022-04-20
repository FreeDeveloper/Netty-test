package com.smile.order.serialization;

import com.smile.order.common.Message;
import org.nustaq.serialization.FSTConfiguration;

/**
 * FSTConfiguration是线程安全的，可以使用单例
 * @author zhanglei19@corp.netease.com
 * @date 2022-03-14 4:30 下午
 */
public class FSTSerializer {
    static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

    public static byte[] encoder(Message message) {
        return conf.asByteArray(message);
    }

    public static Message decoder(byte[] bytes) {
        Object ob = conf.asObject(bytes);
        return (Message) ob;
    }
}
