package com.smile.demo.vote.codec;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.smile.demo.vote.VoteMsg;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:19 上午
 */
public class VoteMsgTextCodec implements VoteMsgCodec {
    /**
     * Wire格式 "VOTEPROTO" <"v"|"i"> [<RESPFLAG>] <CANDIDATE> [<VOTECNT>]
     * v 标识投票消息 i 标识查询消息
     */
    // 魔数，区分垃圾消息
    public static final String MAGIC = "Voting";
    public static final String VOTE_STR = "v";
    public static final String INQ_STR = "i";
    public static final String RESPONSE_STR = "R";

    public static final String CHARSET_NAME = "ASCII";
    public static final String DELIM_STR = " ";
    public static final int MAX_WIRE_LENGTH = 2000;

    @Override
    public byte[] toWire(VoteMsg vote) throws IOException {
        String msgStr = MAGIC + DELIM_STR + (vote.isInquiry() ? INQ_STR : VOTE_STR)
                + DELIM_STR + (vote.isResponse() ? RESPONSE_STR + DELIM_STR : "")
                + vote.getCandidateID() + DELIM_STR
                + vote.getVoteCount();
        return msgStr.getBytes(CHARSET_NAME);
    }

    @Override
    public VoteMsg fromWire(byte[] input) throws IOException {
        ByteArrayInputStream msgStream = new ByteArrayInputStream(input);
        Scanner s = new Scanner(new InputStreamReader(msgStream, CHARSET_NAME));
        String token = s.next();
        if (!token.equals(MAGIC)) {
            throw new IOException("Bad magic string:" + token);
        }
        VoteMsg voteMsg = new VoteMsg();
        token = s.next();
        if (token.equals(VOTE_STR)) {
            voteMsg.setInquiry(false);
        } else {
            voteMsg.setInquiry(true);
        }

        token = s.next();
        if (token.equals(RESPONSE_STR)) {
            voteMsg.setResponse(true);
            token = s.next();
        } else {
            voteMsg.setResponse(false);
        }
        voteMsg.setCandidateID(Integer.parseInt(token));

        if (voteMsg.isResponse()) {
            token = s.next();
            voteMsg.setVoteCount(Long.parseLong(token));
        }
        return voteMsg;
    }
}
