package com.duobiao.mainframedart.ble.kit;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.duobiao.mainframedart.util.PrefUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class NewBluetoothLe {
    private final static String TAG = "BluetoothLe";// BluetoothLeService.class.getSimpleName();

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";


    // Implements callback methods for GATT events that the app cares about. For
    // example,
    // connection change and services discovered.
    private static NewBluetoothLe bluetoothLe;


    private Context context;
    private Timer bleConnCheckTimer;

    private List<BleStatusListener> statusListeners;
    private List<BleDataListener> mBleDataListeners;

    private Handler mHandler;

    private BleDevice currDevice;
    private boolean lock = false;

    public void lock() {
        lock = true;
    }

    public void unlock() {
        lock = false;
    }

    private NewBluetoothLe(Context context) {
        this.context = context;
        mHandler = new Handler();
        statusListeners = new ArrayList<>();
        mBleDataListeners = new ArrayList<>();
    }

    public void startBleService() {
        if (bleConnCheckTimer == null) {
            bleConnCheckTimer = new Timer();
            try {
                getDevice();
                bleConnCheckTimer.scheduleAtFixedRate(new CheckTimerTask(), 0, 8000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopBleService() {
        disconnect();
        if (bleConnCheckTimer != null) {
            bleConnCheckTimer.cancel();
            bleConnCheckTimer = null;
        }
    }

    public class CheckTimerTask extends TimerTask {
        @Override
        public void run() {
            if (BleManager.getInstance().isSupportBle() && BleManager.getInstance().isBlueEnable()) {
//                if (MyInfoUtil.getInstance().isAccountInfoExist()) {
                    if (!isConnect()) {
                        connect();
                    } else {
                        if (currDevice != null && statusListeners != null && !statusListeners.isEmpty()) {
                            //                            mHandler.post(() -> {
                            //                                for (BleStatusListener listener : statusListeners) {
                            //                                    if (!(listener instanceof NewBleActivity))
                            //                                        listener.onConnected(currDevice);
                            //                                }
                            //                            });
                        }
                    }
//                } else {
//                    print("---ble 用户未登陆");
//                }
            } else {
                print("---ble 未打开");
            }
        }
    }


    public static NewBluetoothLe getInstance(Context context) {
        if (bluetoothLe == null) {
            synchronized (NewBluetoothLe.class) {
                if (bluetoothLe == null) bluetoothLe = new NewBluetoothLe(context);
            }
        }
        return bluetoothLe;
    }


    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        BleManager.getInstance().disconnectAllDevice();
    }

    private void print(String msg) {
        Log.i(TAG, "==allan=== ble " + msg);
    }

    /**
     * 不能同时执行扫描链接
     *
     * @return
     */
    public synchronized boolean connect() {
        if (lock) return false;
        lock();
        BleManager.getInstance().scan(callback);
        return true;
    }


    private BleScanCallback callback = new BleScanCallback() {

        @Override
        public void onScanStarted(boolean success) {
            print("开始扫描");
            if (!(BleManager.getInstance().isSupportBle() && BleManager.getInstance().isBlueEnable())) {
                unlock();
            }
        }

        @Override
        public void onLeScan(BleDevice bleDevice) {
            super.onLeScan(bleDevice);
        }

        @Override
        public void onScanning(BleDevice bleDevice) {
            print("---ble name:" + bleDevice.getName() + "    mac:" + bleDevice.getMac());
            if (getDevice() == null) {
                if (!TextUtils.isEmpty(getBleName(bleDevice.getName()))) {
                    BleManager.getInstance().cancelScan();
                }
            } else {
                if (getDevice().getMac().equals(bleDevice.getMac())) {
                    BleManager.getInstance().cancelScan();
                }
            }
        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            unlock();
            print("扫描结束：共有：" + scanResultList.size());
            unlock();
            if (getDevice() == null) {
                if (scanResultList != null) {
                    for (BleDevice device : scanResultList) {
                        if (!TextUtils.isEmpty(getBleName(device.getName()))) {
                            //链接第一个靶
                            saveDeviceToCache(device);
                            connectWithBleManager(device);
                            break;
                        }
                    }
                }
            } else {
                for (BleDevice device : scanResultList) {
                    if (device.getMac().equals(getDevice().getMac())) {
                        connectWithBleManager(device);
                        break;
                    }
                }
            }
        }
    };


    /**
     * 通过蓝牙device设备链接
     *
     * @param bleDevice 设备
     */
    public void connectWithBleManager(final BleDevice bleDevice) {
        Log.i("tag", "正在调用connect连接" + bleDevice.getMac());
        disconnect();
        BleManager.getInstance().connect(bleDevice, bleConnectCallBack);
    }

    public boolean isConnect() {
        boolean flag = false;
        BleDevice device = getDevice();
        if (device != null) {
            try {
                flag = BleManager.getInstance().isConnected(device);
            } catch (Exception e) {
            }
        }
        //        if (flag) {
        //            Log.i(TAG, "---ble 链接成功---");
        //        } else {
        //            Log.i(TAG, "---ble 链接失败---");
        //        }
        return flag;
    }

    public BleDevice getDevice() {
        if (currDevice == null) {
            currDevice = getDeviceFromCache();
        }
        return currDevice;
    }

    private BleDevice getDeviceFromCache() {
        String json = PrefUtils.getString(context, "blue_device", "");
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, BleDevice.class);
        }
        return null;
    }

    public void saveDeviceToCache(BleDevice device) {
        if (device != null) {
            currDevice = device;
            String json = JSON.toJSONString(device);
            PrefUtils.putString(context, "blue_device", json);
        }
    }

    /**
     * 临时解决问题
     */
    public void sendBleStatus() {
        BleDevice device = getDevice();
        if (device != null) {
            try {
                if (isConnect()) {
                    sendBleStatus(device, 1);
                } else {
                    sendBleStatus(device, 0);
                }
            } catch (Exception e) {
            }
        }
    }


    private BleGattCallback bleConnectCallBack = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            print("---ble start connect");
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            print("---ble conn fail  " + bleDevice.getMac());
            sendBleStatus(bleDevice, 0);
            if (statusListeners != null && !statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onDisConnected(bleDevice);
                }
            }

        }

        @Override
        public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
            print("---ble conn mac:" + bleDevice.getMac());
            sendBleStatus(bleDevice, 1);
            BluetoothUtil.setBlueConfig(bleDevice.getName().toLowerCase());
            if (statusListeners != null && !statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onConnected(bleDevice);
                }
            }

        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
            print("---ble dis connect mac:" + bleDevice.getMac() + "  statu:" + status);
            sendBleStatus(bleDevice, 0);
            if (statusListeners != null && !statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onDisConnected(bleDevice);
                }
            }

        }
    };

    public void addBleDataListener(BleDataListener listener) {
        setDataListener(listener, true);
    }

    public void removeBleDataListener(BleDataListener listener) {
        setDataListener(listener, false);
    }

    private synchronized void setDataListener(BleDataListener listener, boolean isAdd) {
        if (mBleDataListeners == null) return;
        if (isAdd) {
            if (!mBleDataListeners.contains(listener)) mBleDataListeners.add(listener);
            BleDevice device = getDevice();
            if (device != null) {
                print("--ble 设置监听>>>>>>>>>>>>>>>>>>>");
                BleManager.getInstance().notify(device, SampleGattAttributes.CLIENT_DUOBIAO_SOCKET_CHARACTERISTIC_CONFIG, SampleGattAttributes.CLIENT_DUOBIAO_GATT_CHARACTERISTIC_CONFIG, notifyCallback);
            }

        } else {
            if (mBleDataListeners.contains(listener)) mBleDataListeners.remove(listener);
            BleDevice device = getDevice();
            if (device != null) {
                print("--ble 移除监听>>>>>>>>>>>>>>>>>>>");
                BleManager.getInstance().stopNotify(device, SampleGattAttributes.CLIENT_DUOBIAO_SOCKET_CHARACTERISTIC_CONFIG, SampleGattAttributes.CLIENT_DUOBIAO_GATT_CHARACTERISTIC_CONFIG);
            }
        }
    }


    /**
     * 监听回调callback
     */
    private BleNotifyCallback notifyCallback = new BleNotifyCallback() {
        StringBuffer sbEagledart = new StringBuffer();

        @Override
        public void onNotifySuccess() {
        }

        @Override
        public void onNotifyFailure(BleException exception) {
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            //StringBuffer mBuffer = new StringBuffer(); eagledart
            final StringBuffer sb = new StringBuffer();
            for (int length = data.length, i = 0; i < length; ++i) {
                sb.append(String.format("%02X", data[i]));
            }
            //mBuffer.append(sb.toString().toLowerCase());
            String string = sb.toString().toLowerCase();
            //System.out.println("######################" + string);
            print("---ble 数据：" + string);

            //            final Intent intent = new Intent(ACTION_DATA_AVAILABLE);
            //            intent.putExtra(EXTRA_FULL_DATA, string);
            //            context.sendBroadcast(intent);
            String name = BluetoothUtil.getmDartTargetName();
            if (null != name && !name.equals("") && name.toLowerCase().equals("eagledart") && string.length() != 36 && !string.equals("2421060102")) {
                if (string.startsWith("24210602")) {
                    sbEagledart.setLength(0);
                    sbEagledart.append(string);
                } else if (sbEagledart.length() > 0) {
                    sbEagledart.append(string);
                }
                if (mBleDataListeners != null && !mBleDataListeners.isEmpty()) {
                    for (BleDataListener listener : mBleDataListeners) {
                        listener.getBluetoothData(sbEagledart.toString());
                    }
                }

            } else {
                sbEagledart.setLength(0);
                if (mBleDataListeners != null && !mBleDataListeners.isEmpty()) {
                    for (BleDataListener listener : mBleDataListeners) {
                        listener.getBluetoothData(string);
                    }
                }
            }

        }
    };

    /**
     * @param device
     * @param status 1 online  0 offline
     */
    private void sendBleStatus(BleDevice device, int status) {
//        IMNWSocketConnect socketConnect = IMNWManager.getInstance().getSocketConnect();
//        if (socketConnect.getStatus() > 0) {
//            GameMsg gameMsg = GameMessageProxy.getInstance().createChat(null);
//            gameMsg.setType("setBlueStatus");
//            HashMap<String, Object> map = new HashMap<>();
//            map.put("status", status);
//            map.put("macAddress", device.getMac());
//            map.put("name", device.getName());
//            gameMsg.sendTypeChat(map);
//        }
    }


    public boolean addBleStatusListener(BleStatusListener listener) {
        if (statusListeners != null && !statusListeners.contains(listener)) {
            statusListeners.add(listener);
            return true;
        }
        return false;
    }

    public void removeBleStatusListener(BleStatusListener listener) {
        if (statusListeners != null && statusListeners.contains(listener))
            statusListeners.remove(listener);
    }


    public static String getBleName(String bleName) {
        if (bleName == null) {
            bleName = "";
        }
        String nameTmp;
        switch (bleName) {
            case "DuoBiaoDarts":
                nameTmp = "DB-2";
                break;
            case "duobiao_h_01_01":
                nameTmp = "DB-6";
                break;
            case "DuoBiao_h_01_01":
                nameTmp = "DB-6";
                break;
            case "EAGLEDART":
                nameTmp = "DB-1";
                break;
            case "DuoBiao_h01_01":
                nameTmp = "DB-6";
                break;
            default:
                nameTmp = "";
                break;
        }
        return nameTmp;
    }


    public interface BleDataListener {
        void getBluetoothData(String message);
    }

    /**
     * 界面获取蓝牙监听器
     */
    public interface BleStatusListener {
        void onConnected(BleDevice device);//连接成功

        void onDisConnected(BleDevice device);//连接失败
    }
}