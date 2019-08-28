package com.duobiao.mainframedart;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.duobiao.mainframedart.ble.kit.NewBluetoothLe;
import com.duobiao.mainframedart.util.HotFixEngine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Author:Admin
 * Time:2019/7/26 11:06
 * 描述：
 */
public class DartApplication extends Application {
    public static final String FIX_DEX_PATH="wgw";
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

    /**
     * 用下载的dex文件，修复app
     */
    private void loadAllDex() {
        File dexFilePath = getDir(FIX_DEX_PATH, Context.MODE_PRIVATE);
        for (File dexFile:dexFilePath.listFiles()) {
            Log.d("wgw_=====>",dexFile.getAbsolutePath());
            if (dexFile.getAbsolutePath().endsWith("dex")){
                new HotFixEngine().loadDex(this,dexFile);
            }
        }
    }

    @SuppressLint("LongLogTag")
    private void copyDexFileToAppAndFix(Context context, String dexFileName) {
        File path = new File(Environment.getExternalStorageDirectory(),dexFileName);
        Log.d("wgw_copyDexFileToAppAndFix","---"+path.getAbsolutePath());
        if (!path.exists()){
            Toast.makeText(context,"no found file",Toast.LENGTH_LONG).show();
            return;
        }
        if (!path.getAbsolutePath().endsWith("dex")){
            Toast.makeText(context,"file is wrong",Toast.LENGTH_LONG).show();
            return;
        }
        File dexFilePath = context.getDir(FIX_DEX_PATH,Context.MODE_PRIVATE);
        File dexFile = new File(dexFilePath,dexFileName);
        if (dexFile.exists()){
            dexFile.delete();
        }
        InputStream is = null;
        FileOutputStream os = null;
        try{
            is = new FileInputStream(path);
            os = new FileOutputStream(dexFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = is.read(buffer)) != -1){
                os.write(buffer,0,len);
            }
            path.delete();//删除sdcard中的补丁文件，或者你可以直接下载到app的路径中
            is.close();
            os.close();
        } catch (Exception e){
            e.printStackTrace();
            Log.d("wgw_=====>",e.getMessage());
        } finally {
            if (is != null){
                try{
                    is.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            if (os != null){
                try{
                    os.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
