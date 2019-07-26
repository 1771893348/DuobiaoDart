package com.duobiao.mainframedart;

import android.app.Application;

import com.clj.fastble.BleManager;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.duobiao.mainframedart.ble.kit.NewBluetoothLe;

/**
 * Author:Admin
 * Time:2019/7/26 11:06
 * 描述：
 */
public class DartApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BleManager.getInstance().init(this);
        BleManager.getInstance().enableLog(true).setReConnectCount(3, 3000).setOperateTimeout(5000);
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder().setAutoConnect(false)      // 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(3000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        NewBluetoothLe.getInstance(getApplicationContext());
    }
}
