package com.duobiao.mainframedart.ble.bean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:Admin
 * Time:2019/3/22 17:56
 * 描述：
 */
public class DartsTable {

    private String DartTargetName;//镖靶名字
    private String ServiceUUID;//蓝牙serviceID
    private ArrayList<String> CharacteristicUUID;//蓝牙UUID
    private HashMap<String, String> ScoreDict;//扇区对应表

    public String getDartTargetName() {
        return DartTargetName;
    }

    public void setDartTargetName(String dartTargetName) {
        DartTargetName = dartTargetName;
    }

    public String getServiceUUID() {
        return ServiceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        ServiceUUID = serviceUUID;
    }

    public ArrayList<String> getCharacteristicUUID() {
        return CharacteristicUUID;
    }

    public void setCharacteristicUUID(ArrayList<String> characteristicUUID) {
        CharacteristicUUID = characteristicUUID;
    }

    public HashMap<String, String> getScoreDict() {
        return ScoreDict;
    }

    public void setScoreDict(HashMap<String, String> scoreDict) {
        ScoreDict = scoreDict;
    }
}
