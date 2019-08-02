package com.duobiao.mainframedart.game.local;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author:Admin
 * Time:2019/7/26 16:22
 * 描述：
 */
public class LocalMainActivity extends BaseActivity {
    @BindView(R.id.top_title_back)
    ImageView top_title_back;
    @BindView(R.id.top_title_setting)
    ImageView top_title_setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_local_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.top_title_back)
    public void onLocalBack(){
        LocalMainActivity.this.finish();
    }

    @OnClick(R.id.top_title_setting)
    public void onGoSetting(){
        Intent intent = new Intent();
        intent.setClass(this,LocalSeatActivity.class);
        startActivity(intent);
    }
}
