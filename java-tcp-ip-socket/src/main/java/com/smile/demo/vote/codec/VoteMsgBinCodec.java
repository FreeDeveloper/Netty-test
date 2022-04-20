package com.smile.demo.vote.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.smile.demo.vote.VoteMsg;

/**
 * 二进制的编解码
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:36 上午
 */
public class VoteMsgBinCodec implements VoteMsgCodec {
    public static final int MIN_WIRE_LENGTH = 4;
    public static final int MAX_WIRE_LENGTH = 16;
    public static final int MAGIC = 0x5400;
    public static final int MAGIC_MASK = 0xfc00;
    public static final int MAGIC_SHIFT = 8;
    public static final int RESPONSE_FLAG = 0x0200;
    public static final int INQUIRE_FLAG = 0x0100;

    @Override
    public byte[] toWire(VoteMsg vote) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(byteStream);

        short magicAndFlags = MAGIC;
        if (vote.isInquiry()) {
            magicAndFlags |= INQUIRE_FLAG;
        }

        if (vote.isResponse()) {
            magicAndFlags |= RESPONSE_FLAG;
        }

        out.writeShort(magicAndFlags);
        out.writeShort(vote.getCandidateID());
        if (vote.isResponse()) {
            out.writeLong(vote.getVoteCount());
        }
        out.flush();

        return byteStream.toByteArray();
    }

    @Override
    public VoteMsg fromWire(byte[] input) throws IOException {
        ByteArrayInputStream bs = new ByteArrayInputStream(input);
        DataInputStream in = new DataInputStream(bs);

        int magic = in.readShort();
        if ((magic & MAGIC_MASK) != MAGIC) {
            throw new IOException("Bad magic string:" + ((magic & MAGIC_MASK) >> MAGIC_SHIFT));
        }

        boolean resp = (magic & RESPONSE_FLAG) != 0;
        boolean inq = (magic & INQUIRE_FLAG) != 0;
        int candidateID = in.readShort();

        long count = 0;
        if (resp) {
            count = in.readLong();
        }

        return new VoteMsg(inq, resp, candidateID, count);
    }
}
