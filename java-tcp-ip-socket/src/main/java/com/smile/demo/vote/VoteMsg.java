package com.smile.demo.vote;

/**
 * 投票信息
 * @author zhanglei19@corp.netease.com
 * @date 2022-02-27 11:15 上午
 */
public class VoteMsg {
    // true标识查询，false标识投票
    private boolean isInquiry;
    // true标识从服务端的相应
    private boolean isResponse;
    // 候选人ID 0~1000
    private int candidateID;
    //选票数
    private long voteCount;

    public VoteMsg() {
    }

    public VoteMsg(boolean isInquiry, boolean isResponse, int candidateID, long voteCount) {
        this.isInquiry = isInquiry;
        this.isResponse = isResponse;
        this.candidateID = candidateID;
        this.voteCount = voteCount;
    }

    public boolean isInquiry() {
        return isInquiry;
    }

    public void setInquiry(boolean inquiry) {
        isInquiry = inquiry;
    }

    public boolean isResponse() {
        return isResponse;
    }

    public void setResponse(boolean response) {
        isResponse = response;
    }

    public int getCandidateID() {
        return candidateID;
    }

    public void setCandidateID(int candidateID) {
        this.candidateID = candidateID;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public String toString() {
        return com.google.common.base.Objects.toStringHelper(this)
                .add("isInquiry", isInquiry)
                .add("isResponse", isResponse)
                .add("candidateID", candidateID)
                .add("voteCount", voteCount)
                .toString();
    }
}
