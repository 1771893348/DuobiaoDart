package com.duobiao.mainframedart.game.bean;

/**
 * Author:Admin
 * Time:2019/8/2 10:17
 * 描述：
 */
public class RoundDarts {
    private int roundIndex;
    private int roundScoreSum;
    private DartBean[] roundDarts = new DartBean[3];

    public int getRoundIndex() {
        return roundIndex;
    }

    public void setRoundIndex(int roundIndex) {
        this.roundIndex = roundIndex;
    }

    public int getRoundScoreSum() {
        return roundScoreSum;
    }

    public void setRoundScoreSum(int roundScoreSum) {
        this.roundScoreSum = roundScoreSum;
    }

    public DartBean[] getRoundDarts() {
        return roundDarts;
    }

    public void setRoundDarts(DartBean[] roundDarts) {
        this.roundDarts = roundDarts;
    }
}
