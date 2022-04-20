package com.smile.demo.vote;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:46 上午
 */
public class VoteService {
    private Map<Integer, Long> results = new HashMap<>();

    public VoteMsg handleRequest(VoteMsg msg) {
        if (msg.isResponse()) {
            return msg;
        }

        msg.setResponse(true);
        int candidate = msg.getCandidateID();
        Long count = results.get(candidate);

        if (count == null) {
            count = 0L;
        }

        if (!msg.isInquiry()) {
            results.put(candidate, ++count);
        }
        msg.setVoteCount(count);
        return msg;
    }
}
