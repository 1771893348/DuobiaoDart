/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duobiao.mainframedart.ble.kit;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.duobiao.mainframedart.ble.AppContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
@Deprecated
public class BluetoothLe {// extends Service {
    private final static String TAG = "BluetoothLe";// BluetoothLeService.class.getSimpleName();
    private UUID READ_UUID;
    private UUID ServiceUUID;
    private UUID WRITE_UUID;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    private String mDeviceAddress;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTTING = "com.example.bluetooth.le.ACTION_GATT_CONNECTTING";
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String EXTRA_FULL_DATA = "com.example.bluetooth.le.EXTRA_FULL_DATA";

    public final static String ACTION_CLOSE_SERVICE = "com.example.bluetooth.le.ACTION_CLOSE_SERVICE";
    public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    // Implements callback methods for GATT events that the app cares about. For
    // example,
    // connection change and services discovered.
    private static BluetoothLe bluetoothLe;

    private Context context;


    private StringBuffer mBuffer;

    private Handler mHandler;
    private static BleManager bleManager;
    private BleNotifyListener bleNotifyListener;
    private ArrayList<BleStatusListener> statusListeners;

    private BluetoothLe(Context context) {
        super();
        // TODO Auto-generated constructor stub
        this.context = context;
        this.canThrow = true;
        mBuffer = new StringBuffer();
        onCreate();
    }

    /**
     * 初始化蓝牙监听
     */
    public void init() {
        statusListeners = new ArrayList<>();
    }

    public static BluetoothLe getInstance(Context context) {
        if (bluetoothLe == null) {
            bluetoothLe = new BluetoothLe(context);
        }
        return bluetoothLe;
    }

    public BleManager getBleManager() {
        if (bleManager == null) {
            bleManager = BleManager.getInstance();
        }
        return bleManager;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;

                Log.i(TAG, "Connected to GATT server.----------------------");
                broadcastUpdate(ACTION_GATT_CONNECTED);

                Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;

                Log.i(TAG, "Disconnected from GATT server.---------------------");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.w(TAG, "onServicesDiscovered 中: " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                displayGattServices(getSupportedGattServices());
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is
        // carried out as per profile specifications:
        // http://developer.bluetooth.org/gatt/characteristics/Pages/CharacteristicViewer.aspx?u=org.bluetooth.characteristic.heart_rate_measurement.xml
        // if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
        // int flag = characteristic.getProperties();
        // int format = -1;
        // if ((flag & 0x01) != 0) {
        // format = BluetoothGattCharacteristic.FORMAT_UINT16;
        // Log.d(TAG, "Heart rate format UINT16.");
        // } else {
        // format = BluetoothGattCharacteristic.FORMAT_UINT8;
        // Log.d(TAG, "Heart rate format UINT8.");
        // }
        // final int heartRate = characteristic.getIntValue(format, 1);
        // Log.d(TAG, String.format("Received heart rate: %d", heartRate));
        // intent.putExtra(EXTRA_DATA, String.valueOf(heartRate));
        // } else {
        {
            // For all other profiles, writes the data formatted in HEX.

            final byte[] value = characteristic.getValue();
            final StringBuffer sb = new StringBuffer();
            for (int length = value.length, i = 0; i < length; ++i) {
                sb.append(String.format("%02X", value[i]));
            }

            mBuffer.append(sb.toString());
            Log.i(TAG, "reveieve bluetooth device nofity msg : " + mBuffer.toString());
            mHandler.postDelayed((Runnable) new Runnable() {
                @Override
                public void run() {
                    checkCommand(intent);
                    // 新靶机  checkCommand1(intent);
                }
            }, 5L);


        }

    }

    /*
     * public class LocalBinder extends Binder { BluetoothLeService getService()
     * { return BluetoothLeService.this; } }
     */

    /*
     * @Override public IBinder onBind(Intent intent) { return mBinder; }
     *
     * @Override public boolean onUnbind(Intent intent) { // After using a given
     * device, you should make sure that // BluetoothGatt.close() is called //
     * such that resources are cleaned up properly. In this particular //
     * example, close() is // invoked when the UI is disconnected from the
     * Service. close(); return super.onUnbind(intent); }
     */
    // private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    /*
     * @Override public int onStartCommand(Intent intent, int flags, int
     * startId) { // TODO Auto-generated method stub
     * super.onStartCommand(intent, flags, startId);
     *
     * Bundle bundle = (Bundle) intent.getExtras(); mDeviceAddress =
     * bundle.getString(Constant.EXTRAS_DEVICE_ADDRESS);
     *
     *
     * new Thread(new Runnable() {
     *
     * @Override public void run() { connect(mDeviceAddress); } }).start();
     *
     *
     * return startId; }
     */

    // @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        Log.i("onCreate-----------", "");
        if (!initialize()) {
            Log.e("----", "Unable to initialize Bluetooth");
            // finish();
        } else {
            // context.registerReceiver(mGattUpdateReceiver,
            // makeGattUpdateIntentFilter());
        }

    }

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context
                    .getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */

    public boolean connect(final String address) {

        mDeviceAddress = address;
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

//        // Previously connected device. Try to reconnect.
//        if (mBluetoothDeviceAddress != null
//                && address.equals(mBluetoothDeviceAddress)
//                && mBluetoothGatt != null) {
//            Log.d(TAG,
//                    "Trying to use an existing mBluetoothGatt for connection.");
//            if (mBluetoothGatt.connect()) {
//                mConnectionState = STATE_CONNECTING;
//                Log.w(TAG, "mBluetoothGatt.connect()---------11111-----true");
//                //	broadcastUpdate(ACTION_GATT_CONNECTED);// lpmo
//                return true;
//            } else {
//                Log.w(TAG, "mBluetoothGatt.connect()----------2222222----false");
//                return false;
//            }
//        }

        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the
        // autoConnect
        // parameter to false.
//        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        mBluetoothGatt = device.connectGatt(context, false, bleConnectCallBack);
        broadcastUpdate(ACTION_GATT_CONNECTTING);

        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;

        this.ServiceUUID = UUID.fromString("D3FAFFF0-3D39-4DE9-B9B4-BFFCE90EEEB5");
        this.READ_UUID = UUID.fromString("D3FAACE4-3D39-4DE9-B9B4-BFFCE90EEEB5");
        this.WRITE_UUID = UUID.fromString("D3FAACE3-3D39-4DE9-B9B4-BFFCE90EEEB5");
//        this.ServiceUUID = UUID.fromString("340A6B99-3F5F-F32F-F6BA-789FC9167D55");
        //新靶机
//        this.ServiceUUID = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
//        this.READ_UUID = UUID.fromString("0000FFF4-0000-1000-8000-00805F9B34FB");
//        this.WRITE_UUID = UUID.fromString("D3FAACE3-3D39-4DE9-B9B4-BFFCE90EEEB5");
        mHandler = new Handler();
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    //写入命令方法
    public void writeCommand(final byte[] value) {
        if (mCommandCharacteristic != null && value != null) {
            mCommandCharacteristic.setValue(value);
            mBluetoothGatt.writeCharacteristic(mCommandCharacteristic);
        }

    }

    Runnable canThrowRunnable;
    private boolean canThrow;

    private void checkCommand(Intent intent) {
        final String string = this.mBuffer.toString();
        Log.i(TAG, "full info : " + string);
        final String[] split = string.split("2421");
        int n = 0;
        int n2 = 0;
        int n3;
        for (int length = split.length, i = 0; i < length; ++i, n2 = n3) {
            final String s = split[i];
            n3 = n2;
            if (!s.isEmpty()) {//090100000D
                final byte[] hexStringToByteArray = AppContext.hexStringToByteArray(s);
                final String byteArrayToHexString = AppContext.byteArrayToHexString(hexStringToByteArray);
                n3 = n2;
                if (hexStringToByteArray.length > 2) {
                    n3 = n2;
                    if (hexStringToByteArray[0] == 6) {
                        if (hexStringToByteArray[1] == 2) {
                            n3 = n2;
                            if (hexStringToByteArray.length >= 16) {
                                final byte[] array = new byte[16];
                                for (int j = 0; j < 16; ++j) {
                                    array[j] = hexStringToByteArray[j];
                                }
                                final int n4 = n3 = 1;
                                if (this.canThrow) {
                                    this.canThrow = false;
//                                    this.mSendToC.send(array);
                                    intent.putExtra(EXTRA_DATA, array);
                                    this.mHandler.postDelayed(this.canThrowRunnable, 1000L);
                                    n3 = n4;
                                }
                            }
                        } else {
                            n3 = n2;
                            if (hexStringToByteArray[1] == 1) {
                                n3 = n2;
                                if (hexStringToByteArray.length >= 3) {
                                    final byte[] array2 = new byte[3];
                                    for (int k = 0; k < 3; ++k) {
                                        array2[k] = hexStringToByteArray[k];
                                    }
                                    n3 = 1;
                                    this.canThrow = true;
//                                     this.mSendToC.send(hexStringToByteArray);
                                    intent.putExtra(EXTRA_DATA, hexStringToByteArray);
                                }
                            }
                        }
                    }
                }
                if (byteArrayToHexString.equals("090100000D")) {
                    Log.d(this.TAG, "======>收到 090100000D");
                    n3 = 1;
//                    this.errorCount = 0;
//                    this.removeNowCommand();
//                    this.checkNextCommand();
                }
                if (byteArrayToHexString.equals("090190019C")) {
                    Log.d(this.TAG, "======>收到 090190019C");
                    n3 = 1;
//                    if (this.errorCount < 2 && this.mCommandList.get(0).getType() == AppContext.CommandType.Def) {
//                        this.mCommandList.get(0).index = 0;
//                        this.checkNextCommand();
//                        ++this.errorCount;
//                    }
//                    else {
////                        this.removeNowCommand();
////                        this.checkNextCommand();
////                        this.errorCount = 0;
//                    }
                }
            }
            if (n3 != 0) {
                this.mBuffer.delete(0, string.length() + n);
            } else {
                n += string.length();
            }
            Log.d("DATA1", mBuffer.toString());
//            intent.putExtra(EXTRA_DATA, mBuffer.toString());
        }

        // todo jack.sj log info
//        byte[] data_available = intent.getByteArrayExtra(BluetoothLe.EXTRA_DATA);
//        if(null != data_available){
//            for (int i = 0; i < data_available.length; i ++ ){
//                Log.i(TAG, "broadcast msg "  + i +" : " + data_available[i]);
//            }
//        }

        intent.putExtra(EXTRA_FULL_DATA, string);

        context.sendBroadcast(intent);
        canThrowRunnable = new Runnable() {
            @Override
            public void run() {
                canThrow = true;

            }
        };
    }


    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

//    /**
//     * Enables or disables notification on a give characteristic.
//     *
//     * @param characteristic Characteristic to act on.
//     * @param enabled        If true, enable notification. False otherwise.
//     */
//    public void setCharacteristicNotification(
//            BluetoothGattCharacteristic characteristic, boolean enabled) {
//        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
//            Log.w(TAG, "BluetoothAdapter not initialized");
//            return;
//        }
//        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
//
//        // This is specific to Heart Rate Measurement.
//        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
//            BluetoothGattDescriptor descriptor = characteristic
//                    .getDescriptor(UUID
//                            .fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
//            descriptor
//                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//            mBluetoothGatt.writeDescriptor(descriptor);
//        }
//    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();
    }

    private BluetoothGattCharacteristic mCommandCharacteristic;


    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;

        //todo Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            //uuid = gattService.getUuid().toString();

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

            //todo Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                uuid = gattCharacteristic.getUuid().toString();
                Log.w(TAG, "搜索出的服务UUID： " + gattCharacteristic.getUuid().toString() + " -----" + gattCharacteristic.getProperties());

                //todo jack.sj  scan all uuid to connect bluetooth device
                if (gattCharacteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                    {
                        Log.w(TAG, "搜索出的服务UUID&&&&&&&&&&&&&&&&&&&&&： " + gattCharacteristic.getUuid().toString());
                        //todo jack.sj bluetooth device notity
//                    boolean isEnableNotification = mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
//                    if (isEnableNotification){
//                        List<BluetoothGattDescriptor> descriptorList = gattCharacteristic.getDescriptors();
//                        if(descriptorList != null && descriptorList.size() > 0) {
//                            for(BluetoothGattDescriptor descriptor : descriptorList) {
//                                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                                mBluetoothGatt.writeDescriptor(descriptor);
//                            }
//                        }
//                    }
                        mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
                        BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                        if (descriptor != null) {
                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            mBluetoothGatt.writeDescriptor(descriptor);
                        }


                        return;
                    }
                }

                if (gattCharacteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_READ) {
                    //todo jack.sj bluetooth device read
                }
                if (gattCharacteristic.getProperties() == BluetoothGattCharacteristic.PROPERTY_WRITE) {
                    //todo jack.sj bluetooth device write
                    mCommandCharacteristic = gattCharacteristic;
                }

//                if (gattService.getUuid().toString().equals(ServiceUUID.toString())) {
//                    final BluetoothGattService service = mBluetoothGatt.getService(this.ServiceUUID);
//                    if (service == null) {
//                        Log.w(TAG, "搜索出的服务失败： "+gattService.getUuid().toString());
//                        return;
//                    }
//
//                    //if(gattCharacteristic.getProperties() == Character.)
//                    if (gattCharacteristic.getUuid().toString().equals(READ_UUID.toString())) {
//                        Log.w(TAG, "特征UUID： "+gattCharacteristic.getUuid().toString());
//                        final BluetoothGattCharacteristic characteristic = service.getCharacteristic(this.READ_UUID);
//                        if (characteristic != null) {
//                            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
//                            final BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                            mBluetoothGatt.writeDescriptor(descriptor);
//                            return;
//                        }
//                    }
//                    if (gattCharacteristic.getUuid().toString().equals(WRITE_UUID.toString())) {
//                        mCommandCharacteristic = service.getCharacteristic(this.WRITE_UUID);
//                    }
//                }

            }


        }

//		final BluetoothGattCharacteristic characteristic = mGattCharacteristics
//				.get(3).get(6);
//
//		final int charaProp = characteristic.getProperties();
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//			// If there is an active notification on a characteristic, clear
//			// it first so it doesn't update the data field on the user
//			// interface.
//			if (mNotifyCharacteristic != null) {
//				setCharacteristicNotification(mNotifyCharacteristic, false);
//				mNotifyCharacteristic = null;
//			}
//			readCharacteristic(characteristic);
//		}
//		if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
//			mNotifyCharacteristic = characteristic;
//			setCharacteristicNotification(characteristic, true);
//		}
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLe.ACTION_CLOSE_SERVICE.equals(action)) {
                disconnect();
                close();

            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLe.ACTION_CLOSE_SERVICE);
        return intentFilter;
    }

    // @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        // super.onDestroy();

        context.unregisterReceiver(mGattUpdateReceiver);
    }


    private void setAutoReceiveData(BluetoothGatt gatt) {
//        try {
//            BluetoothGattService linkLossService = gatt.getService(SERVICE_UUID);
//            BluetoothGattCharacteristic data = linkLossService.getCharacteristic(CHARACTERISTIC_UUID);
//            BluetoothGattDescriptor defaultDescriptor = data.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG));
//            if (null != defaultDescriptor) {
//                defaultDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                bluetoothGatt.writeDescriptor(defaultDescriptor);
//            }
//            bluetoothGatt.setCharacteristicNotification(data, true);
//        } catch (Exception e) {
//            BleLogUtils.appendLog("setAutoReceiveData:" + e.getMessage());
//        }
    }


    /**
     * 通过蓝牙device设备链接
     *
     * @param bleDevice 设备
     */
    public void connectWithBleManager(final BleDevice bleDevice) {
        Log.i("tag", "正在调用connect连接" + bleDevice.getMac());
        mBluetoothGatt = getBleManager().connect(bleDevice.getMac(), bleConnectCallBack);

    }

    /**
     * 通过蓝牙地址链接
     *
     * @param address 蓝牙mac地址
     */
    public void connectWithBleManager(String address) {
        Log.e("tag", "正在调用connect连接" + address);
        mBluetoothGatt = getBleManager().connect(address, bleConnectCallBack);

    }


    private BleGattCallback bleConnectCallBack = new BleGattCallback() {
        @Override
        public void onStartConnect() {
            //progressDialog.show();
            broadcastUpdate(ACTION_GATT_CONNECTTING);
//            bleStatusListener.onConnecting();
            if (!statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onConnecting();
                }
            }
        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
//                img_loading.clearAnimation();
//                img_loading.setVisibility(View.INVISIBLE);
//                btn_scan.setText(getString(R.string.start_scan));
//                progressDialog.dismiss();
//                Toast.makeText(BluetoothActivity.this, "连接失败", Toast.LENGTH_LONG).show();
            Log.i(TAG, "send notify onConnectSuccess:" + bleDevice.getName() + ":" + bleDevice.getMac());
            broadcastUpdate(ACTION_GATT_DISCONNECTED);
//            bleStatusListener.onDisConnected();
            if (!statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onDisConnected();
                }
            }
        }

        @Override
        public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
            Log.i(TAG, "send notify onConnectSuccess:" + bleDevice.getName() + ":" + bleDevice.getMac());
            broadcastUpdate(ACTION_GATT_CONNECTED);
            if (!statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onConnected();
                }
            }

            //HeibaApplication.getInstance().setBleDevice(bleDevice);
            // listenNotify();

        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
            broadcastUpdate(ACTION_GATT_DISCONNECTED);
//            bleStatusListener.onDisConnected();
            if (!statusListeners.isEmpty()) {
                for (BleStatusListener listener : statusListeners) {
                    listener.onDisConnected();
                }
            }
            Log.i(TAG, "send notify onDisConnected:" + bleDevice.getName() + ":" + bleDevice.getMac());
        }
    };

    public void addBlueStatusListener(BleStatusListener listener) {
        //        System.out.println("listener 前= " + mMessageListenerList.size());
        //        if (mMessageListenerList.size() > 0) {
        //            mMessageListenerList.clear();
        //        }
        statusListeners.add(listener);
        //        System.out.println("listener 后= " + mMessageListenerList.size());
    }

    public void removeBlueStatusListener(BleStatusListener listener) {
        statusListeners.remove(listener);
    }

    /**
     * 蓝牙数据监听
     */
    public void listenNotify() {
//        BleManager.getInstance().notify(
//                HeibaApplication.getInstance().getBlueDve(),
//                SampleGattAttributes.CLIENT_DUOBIAO_SOCKET_CHARACTERISTIC_CONFIG,
//                SampleGattAttributes.CLIENT_DUOBIAO_GATT_CHARACTERISTIC_CONFIG, notifyCallback
//        );
    }

    /**
     * 监听回调callback
     */
    private BleNotifyCallback notifyCallback = new BleNotifyCallback() {
        @Override
        public void onNotifySuccess() {
            bleNotifyListener.onNotifySuccess();
        }

        @Override
        public void onNotifyFailure(BleException exception) {
            bleNotifyListener.onNotifyFailure();
        }

        @Override
        public void onCharacteristicChanged(byte[] data) {
            //StringBuffer mBuffer = new StringBuffer();
            final StringBuffer sb = new StringBuffer();
            for (int length = data.length, i = 0; i < length; ++i) {
                sb.append(String.format("%02X", data[i]));
            }
            //mBuffer.append(sb.toString().toLowerCase());
            String string = sb.toString().toLowerCase();
            //System.out.println("######################" + string);


//            final Intent intent = new Intent(ACTION_DATA_AVAILABLE);
//            intent.putExtra(EXTRA_FULL_DATA, string);
//            context.sendBroadcast(intent);
            bleNotifyListener.getBluetoothData(string);
        }
    };

    public BleNotifyListener getBleNotifyListener() {
        return bleNotifyListener;
    }

    /**
     * 设置蓝牙数据监听器
     *
     * @param bleNotifyListener
     */
    public void setBleNotifyListener(BleNotifyListener bleNotifyListener) {
        this.bleNotifyListener = bleNotifyListener;
        listenNotify();
    }

    /**
     * 界面获取蓝牙数据接口
     */
    public interface BleNotifyListener {
        void onNotifySuccess();

        void onNotifyFailure();

        void getBluetoothData(String Message);
    }

    /**
     * 设置蓝牙数据监听器
     *
     * @param bleStatusListener
     */
    public void setBleStatusListener(BleStatusListener bleStatusListener) {
//        this.bleStatusListener = bleStatusListener;
    }

    /**
     * 界面获取蓝牙监听器
     */
    public interface BleStatusListener {
        void onConnected();//连接成功

        void onDisConnected();//连接失败

        void onConnecting();//连接中
    }
}
