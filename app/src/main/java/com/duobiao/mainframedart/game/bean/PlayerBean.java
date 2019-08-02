package com.duobiao.mainframedart.game.bean;

import java.util.ArrayList;

/**
 * Author:Admin
 * Time:2019/8/1 16:32
 * 描述：
 */
public class PlayerBean {
    private String playerName;
    private String playerIcon;
    private int scoreSum;
    private double playerPPD;
    private ArrayList<RoundDarts> rounds = new ArrayList<>();

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerIcon() {
        return playerIcon;
    }

    public void setPlayerIcon(String playerIcon) {
        this.playerIcon = playerIcon;
    }

    public int getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(int scoreSum) {
        this.scoreSum = scoreSum;
    }

    public double getPlayerPPD() {
        return playerPPD;
    }

    public void setPlayerPPD(double playerPPD) {
        this.playerPPD = playerPPD;
    }

    public ArrayList<RoundDarts> getRounds() {
        return rounds;
    }

    public void setRounds(ArrayList<RoundDarts> rounds) {
        this.rounds = rounds;
    }
}
