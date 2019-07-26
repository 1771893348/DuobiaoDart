package com.duobiao.mainframedart;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.duobiao.mainframedart.base.BaseActivity;
import com.duobiao.mainframedart.ble.activity.NewBleActivity;
import com.duobiao.mainframedart.ble.kit.BluetoothUtil;
import com.duobiao.mainframedart.game.local.LocalMainActivity;
import com.duobiao.mainframedart.permission.PermissionHelper;
import com.duobiao.mainframedart.permission.PermissionInterface;
import com.duobiao.mainframedart.view.MainItemView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends BaseActivity implements View.OnClickListener, PermissionInterface {
    private MainItemView item_local_game,item_net_game,item_match_game,item_square_game,item_mall_game,item_setting_game;
    private PermissionHelper mPermissionHelper;
//    private RecyclerView main_page_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    protected void initView(){
        //加载蓝牙数据
        BLEUUIDObj();
        item_local_game = findViewById(R.id.item_local_game);
        item_net_game = findViewById(R.id.item_net_game);
        item_match_game = findViewById(R.id.item_match_game);
        item_square_game = findViewById(R.id.item_square_game);
        item_mall_game = findViewById(R.id.item_mall_game);
        item_setting_game = findViewById(R.id.item_setting_game);
        item_local_game.setMain_item_name("练习赛");
        item_net_game.setMain_item_name("联网");
        item_match_game.setMain_item_name("比赛");
        item_square_game.setMain_item_name("广场");
        item_mall_game.setMain_item_name("商城");
        item_setting_game.setMain_item_name("设置");



        mPermissionHelper = new PermissionHelper(this, this);
        mPermissionHelper.requestPermissions();

    }

    private void initData(){

    }

    protected void initListener(){
        item_local_game.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_local_game:
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this, NewBleActivity.class);
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LocalMainActivity.class);
                startActivity(intent);
                break;
        }
    }
    public void BLEUUIDObj() {
        try {
            InputStreamReader isr = new InputStreamReader(this.getAssets().open("main.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builders = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builders.append(line);
            }
            br.close();
            isr.close();
            BluetoothUtil.initBlueConfig(builders.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(mPermissionHelper.requestPermissionsResult(requestCode, permissions, grantResults)){ //权限请求结果，并已经处理了该回调
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public int getPermissionsRequestCode() {
        return 10000;
    }

    @Override
    public String[] getPermissions() {
        return new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
    }

    @Override
    public void requestPermissionsSuccess() {

    }

    @Override
    public void requestPermissionsFail() {

    }
}
