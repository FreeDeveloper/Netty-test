package com.smile.demo.vote.codec;

import java.io.IOException;

import com.smile.demo.vote.VoteMsg;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:18 上午
 */
public interface VoteMsgCodec {
    byte[] toWire(VoteMsg vote) throws IOException;

    VoteMsg fromWire(byte[] input) throws IOException;
}
