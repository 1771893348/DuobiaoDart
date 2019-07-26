package com.duobiao.mainframedart.ble.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.ble.adapter.DeviceAdapter;
import com.duobiao.mainframedart.ble.kit.NewBluetoothLe;
import com.duobiao.mainframedart.util.ToastUtil;
import com.duobiao.mainframedart.view.StrokeTextView;

import java.util.HashMap;
import java.util.Map;



/**
 * 作者： zhaoyl on 2019/2/1.
 */
public class NewBleActivity extends Activity implements View.OnClickListener, NewBluetoothLe.BleStatusListener, Handler.Callback {

    private ImageView homepageInvitaImgCancle;
    private StrokeTextView strokeTextView;
    TextView contentBlue;
    TextView contentBlueAss;

    //列表
    private LinearLayout mLL_scan_select;
    StrokeTextView selectBlue;
    private ListView listView;
    TextView bleScanSelectBt;

    //重连
    private LinearLayout mRL_device;
    private ProgressBar mProgressDialog;
    private ImageView mImageV_ble_status;

    //底部扫描
    private TextView mTxt_connect_other;


    private static final int REQUEST_ENABLE_BT = 1;
    private DeviceAdapter mDeviceAdapter;


    public static String TAG = "BleTools";


    private Handler handler;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isScaning = false;
    private Map<String, Boolean> scanDevices;//不需要顺序


    /**
     * 初始化系统视图
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanDevices = new HashMap<>();
        handler = new Handler(this);
        initApplicationView();
        initApplicationData();
        initApplicationListenner();
        checkBluetoothPermission();
        NewBluetoothLe.getInstance(getApplicationContext()).addBleStatusListener(this);

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
    }

    /**
     * 初始化系统视图
     */
    private void initApplicationView() {
        setContentView(R.layout.bluetooth_new);
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        contentBlue = findViewById(R.id.content_blue);
        contentBlueAss = findViewById(R.id.content_blue_ass);
        listView = findViewById(R.id.ble_scan_select_lv);
        strokeTextView = findViewById(R.id.stroke_tv);
        mTxt_connect_other = findViewById(R.id.tv_connect_other);
        mImageV_ble_status = findViewById(R.id.iv_ble_status);
        mLL_scan_select = findViewById(R.id.ll_scan_select);
        mRL_device = findViewById(R.id.rl_device);
        selectBlue = findViewById(R.id.select_blue);
        bleScanSelectBt = findViewById(R.id.ble_scan_select_bt);
        mProgressDialog = findViewById(R.id.pgb_status);
        homepageInvitaImgCancle = findViewById(R.id.homepage_invita_img_cancle);
        bleScanSelectBt.setOnClickListener(this);
    }

    /**
     * @param type 1 已链接  2 未链接  3 连接中
     */
    private void showBleView(int type) {
        if (mProgressDialog == null) return;
        if (type == 1) {
            bleScanSelectBt.setVisibility(View.GONE);
            mProgressDialog.setVisibility(View.GONE);
            mImageV_ble_status.setVisibility(View.VISIBLE);
            mImageV_ble_status.setImageResource(R.drawable.icon_blu_yes);
            mImageV_ble_status.setTag(1);
            strokeTextView.setText("已连接飞镖机");
        } else if (type == 2) {
            mProgressDialog.setVisibility(View.GONE);
            mImageV_ble_status.setVisibility(View.VISIBLE);
            mImageV_ble_status.setTag(2);
            mImageV_ble_status.setImageResource(R.drawable.icon_blu_no);
            bleScanSelectBt.setVisibility(View.VISIBLE);
            strokeTextView.setText("未找到飞镖机");
        } else if (type == 3) {
            mProgressDialog.setVisibility(View.VISIBLE);
            mImageV_ble_status.setVisibility(View.GONE);
            bleScanSelectBt.setVisibility(View.GONE);
            strokeTextView.setText("正在连接飞镖机");
        }
        mRL_device.setVisibility(View.VISIBLE);
        mLL_scan_select.setVisibility(View.GONE);
    }

    private void showBleListView(boolean isShow) {
        if (isShow) {
            mLL_scan_select.setVisibility(View.VISIBLE);
            mRL_device.setVisibility(View.GONE);
        } else {
            mLL_scan_select.setVisibility(View.GONE);
            mRL_device.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化数据并设置数据适配器
     */
    protected void initApplicationData() {
        this.mDeviceAdapter = new DeviceAdapter(this);
        this.listView.setAdapter(mDeviceAdapter);
        BleDevice device = NewBluetoothLe.getInstance(getApplicationContext()).getDevice();
        if (device != null) {
            contentBlue.setText(NewBluetoothLe.getBleName(device.getName()));
            contentBlueAss.setText(device.getMac());
        }
        if (NewBluetoothLe.getInstance(getApplicationContext()).isConnect()) {
            showBleView(1);
        } else {
            showBleView(2);
        }
    }

    /**
     * 初始化监听器
     */
    protected void initApplicationListenner() {
        this.bleScanSelectBt.setOnClickListener(this);
        this.homepageInvitaImgCancle.setOnClickListener(this);
        this.mTxt_connect_other.setOnClickListener(this);
        this.mImageV_ble_status.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                BleDevice device = mDeviceAdapter.getItem(position);
                NewBluetoothLe.getInstance(getApplicationContext()).saveDeviceToCache(device);
                contentBlue.setText(NewBluetoothLe.getBleName(device.getName()));
                contentBlueAss.setText(device.getMac());
                showBleView(3);
                handler.sendEmptyMessage(0x66);
                showBleListView(false);
                scanDevices.clear();
                mDeviceAdapter.clear();
                if (isScaning && mBluetoothAdapter != null) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    handler.removeMessages(0x88);
                }
            }
        });
    }

    /**
     * 按钮点击事件
     *
     * @param v 视图View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ble_scan_select_bt://重新搜索 扫描
                NewBluetoothLe.getInstance(getApplicationContext()).connect();
                showBleView(3);
                handler.sendEmptyMessageDelayed(0x77, 10000);
                break;
            case R.id.homepage_invita_img_cancle://关闭蓝牙显示界面
                finish();
                break;
            case R.id.tv_connect_other://连接其他设备 扫描
                bleScan();
                break;
            case R.id.iv_ble_status://点击重试
                break;
        }
    }

    private void bleScan() {
        if (mBluetoothAdapter != null) {
            showBleListView(true);
            //mDeviceAdapter.updateList(new ArrayList<>());
            selectBlue.setText("正在扫描");
            bleScanSelectBt.setVisibility(View.INVISIBLE);
            scanDevices.clear();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            isScaning = true;
            handler.sendEmptyMessageDelayed(0x88, 15000);
        } else {
//            ToastSingle.getInstance().show("手机不支持蓝牙");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ENABLE_BT) {

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//蓝牙权限开启成功

            } else {
                Toast.makeText(this, "蓝牙权限未开启!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        NewBluetoothLe.getInstance(getApplicationContext()).removeBleStatusListener(this);
        if (isScaning && mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            handler.removeMessages(0x88);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NewBluetoothLe.getInstance(getApplicationContext()).sendBleStatus();
    }


    //------------------------------自定义私有函数段------------------------------

    /**
     * 校验蓝牙权限
     */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_BT);
            } else {//权限已打开
                if (!BleManager.getInstance().isBlueEnable()) {
                    this.startActivityForResult(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS), 0x123);
                    ToastUtil.showShortToast(this, "请开启蓝牙选项！");

                }
            }
        } else {//小于23版本直接使用
            if (!BleManager.getInstance().isBlueEnable()) {
                ToastUtil.showShortToast(this, "请开启蓝牙选项！");
                BleManager.getInstance().enableBluetooth();
            }
        }
    }

    @Override
    public void onConnected(BleDevice device) {
        if (!isFinishing()) showBleView(1);
    }

    @Override
    public void onDisConnected(BleDevice device) {
        if (!isFinishing()) {
            BleDevice currDevice = NewBluetoothLe.getInstance(getApplicationContext()).getDevice();
            if (device.getMac().equals(currDevice.getMac())) showBleView(2);
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        if (isFinishing()) return false;
        if (message.what == 0x88) {
            if (mBluetoothAdapter != null) mBluetoothAdapter.stopLeScan(mLeScanCallback);
            //刷新列表
            bleScanSelectBt.setVisibility(View.VISIBLE);
            if (scanDevices.size() <= 0) {
                selectBlue.setText("没有发现飞镖机");
            } else {
                selectBlue.setText("选择飞镖机");
                mDeviceAdapter.notifyDataSetChanged();
            }
        } else if (message.what == 0x66) {
            if (!NewBluetoothLe.getInstance(getApplicationContext()).connect()) {
                handler.sendEmptyMessageDelayed(0x66, 1000);
            }
        } else if (message.what == 0x77) {
            if (NewBluetoothLe.getInstance(getApplicationContext()).isConnect()) {
                showBleView(1);
            } else {
                showBleView(2);
            }
        }
        return false;
    }


    final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "name:" + device.getName() + "  mac:" + device.getAddress());
            String name = mDeviceAdapter.getBleName(device.getName());
            if (!name.equals("")) {
                if (!scanDevices.containsKey(device.getAddress())) {
                    mDeviceAdapter.addData(new BleDevice(device, rssi, scanRecord, 0));
                    scanDevices.put(device.getAddress(), true);
                }
            }
        }

    };
}
