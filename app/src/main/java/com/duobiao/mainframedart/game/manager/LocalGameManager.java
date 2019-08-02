package com.duobiao.mainframedart.game.manager;

import com.duobiao.mainframedart.ble.kit.BluetoothUtil;
import com.duobiao.mainframedart.game.bean.DartBean;

/**
 * Author:Admin
 * Time:2019/7/30 10:29
 * 描述：
 */
public class LocalGameManager {
    public static DartBean createFromByte(String blue) {
        String areaMark = BluetoothUtil.getAreaMarkOfKey(blue);
        if (null == areaMark || "".equals(areaMark)) return null;

        DartBean g = new DartBean();
        g.turnDartBean(areaMark);

        return g;
    }
}
