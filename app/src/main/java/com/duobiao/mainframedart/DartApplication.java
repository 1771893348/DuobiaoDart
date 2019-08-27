package com.duobiao.mainframedart;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.duobiao.mainframedart.ble.kit.NewBluetoothLe;
import com.duobiao.mainframedart.util.HotFixEngine;

import java.io.File;

/**
 * Author:Admin
 * Time:2019/7/26 11:06
 * 描述：
 */
public class DartApplication extends Application {
    public static final String FIX_DEX_PATH="";
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
        copyDexFileToAppAndFix(this,"path.dex");
        loadAllDex();
    }

    private void loadAllDex() {
        File dexFilePath = getDir(FIX_DEX_PATH, Context.MODE_PRIVATE);
        for (File dexFile:dexFilePath.listFiles()) {
            Log.d("wgw_=====>",dexFile.getAbsolutePath());
            if (dexFile.getAbsolutePath().endsWith("dex")){
                new HotFixEngine().loadDex(this,dexFile);
            }
        }
    }

    private void copyDexFileToAppAndFix(DartApplication dartApplication, String s) {
    }
}
