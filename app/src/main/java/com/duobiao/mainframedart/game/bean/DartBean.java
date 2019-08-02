package com.duobiao.mainframedart.game.bean;

/**
 * Author:Admin
 * Time:2019/7/30 10:30
 * 描述：
 */
public class DartBean {
    private String DartBleString;
    private int DartScore;//1
    private String DartShow;//1X1
    private String DartTag;//1a
    private int areaMark;//1
    private int munMark;
    private boolean effective;//是否有效

    public String getDartBleString() {
        return DartBleString;
    }

    public void setDartBleString(String dartBleString) {
        DartBleString = dartBleString;
    }

    public int getDartScore() {
        return DartScore;
    }

    public void setDartScore(int dartScore) {
        DartScore = dartScore;
    }

    public String getDartShow() {
        return DartShow;
    }

    public void setDartShow(String dartShow) {
        DartShow = dartShow;
    }

    public String getDartTag() {
        return DartTag;
    }

    public void setDartTag(String dartTag) {
        DartTag = dartTag;
    }

    public int getAreaMark() {
        return areaMark;
    }

    public void setAreaMark(int areaMark) {
        this.areaMark = areaMark;
    }

    public int getMunMark() {
        return munMark;
    }

    public void setMunMark(int munMark) {
        this.munMark = munMark;
    }

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    /**
     * 根据区域 计算 基础得分
     *
     * @param areaMark
     * @return
     */
    public void turnDartBean(String areaMark) {
        int score = 0;
        this.DartTag = areaMark;
        switch (areaMark) {
            case "1a":
            case "1b":
            case "1c":
            case "1d":
                this.areaMark = 1;
                break;
            case "2a":
            case "2b":
            case "2c":
            case "2d":
                this.areaMark = 2;
                break;
            case "3a":
            case "3b":
            case "3c":
            case "3d":
                this.areaMark = 3;
                break;
            case "4a":
            case "4b":
            case "4c":
            case "4d":
                this.areaMark = 4;
                break;
            case "5a":
            case "5b":
            case "5c":
            case "5d":
                this.areaMark = 5;
                break;
            case "6a":
            case "6b":
            case "6c":
            case "6d":
                this.areaMark = 6;
                break;
            case "7a":
            case "7b":
            case "7c":
            case "7d":
                this.areaMark = 7;
                break;
            case "8a":
            case "8b":
            case "8c":
            case "8d":
                this.areaMark = 8;
                break;
            case "9a":
            case "9b":
            case "9c":
            case "9d":
                this.areaMark = 9;
                break;
            case "10a":
            case "10b":
            case "10c":
            case "10d":
                this.areaMark = 10;
                break;
            case "11a":
            case "11b":
            case "11c":
            case "11d":
                this.areaMark = 11;
                break;
            case "12a":
            case "12b":
            case "12c":
            case "12d":
                this.areaMark = 12;
                break;
            case "13a":
            case "13b":
            case "13c":
            case "13d":
                this.areaMark = 13;
                break;
            case "14a":
            case "14b":
            case "14c":
            case "14d":
                this.areaMark = 14;
                break;
            case "15a":
            case "15b":
            case "15c":
            case "15d":
                this.areaMark = 15;
                break;
            case "16a":
            case "16b":
            case "16c":
            case "16d":
                this.areaMark = 16;
                break;
            case "17a":
            case "17b":
            case "17c":
            case "17d":
                this.areaMark = 17;
                break;
            case "18a":
            case "18b":
            case "18c":
            case "18d":
                this.areaMark = 18;
                break;
            case "19a":
            case "19b":
            case "19c":
            case "19d":
                this.areaMark = 19;
                break;
            case "20a":
            case "20b":
            case "20c":
            case "20d":
                this.areaMark = 20;

                break;
            case "g":
                this.areaMark = 50;
                break;
            case "f":
                this.areaMark = 25;
                break;
            case "h":
            case "e":
            case "j":
            case "x"://todo 服务器端miss标记
                this.areaMark = 0;
                break;
        }
        turnArea(areaMark);
    }
    private void turnArea(String areaMark){
        if (areaMark.endsWith("a") || areaMark.endsWith("c")){
            this.DartShow = this.areaMark+" X 1";
            this.DartScore = this.areaMark*1;
            this.munMark = 1;
        }else if (areaMark.endsWith("b")){
            this.DartShow = this.areaMark+" X 3";
            this.DartScore = this.areaMark*3;
            this.munMark = 3;
        }else if (areaMark.endsWith("d")){
            this.DartShow = this.areaMark+" X 2";
            this.DartScore = this.areaMark*2;
            this.munMark = 2;
        }else if (areaMark.endsWith("g")){
            this.DartShow = "25 X 2";
            this.DartScore = 25*2;
            this.munMark = 2;
        }else if (areaMark.endsWith("f")){
            this.DartShow = this.areaMark+" X 1";
            this.DartScore = this.areaMark*1;
            this.munMark = 1;
        }else if (areaMark.endsWith("h") || areaMark.endsWith("e") || areaMark.endsWith("j") || areaMark.endsWith("x")){
            this.DartShow = "MISS";
            this.DartScore = this.areaMark;
            this.munMark = 0;
        }
    }
}
