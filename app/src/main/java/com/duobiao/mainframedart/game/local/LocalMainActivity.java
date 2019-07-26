package com.duobiao.mainframedart.game.local;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:Admin
 * Time:2019/7/26 16:22
 * 描述：
 */
public class LocalMainActivity extends BaseActivity {
    @BindView(R.id.top_title_back)
    private ImageView top_title_back;
    @BindView(R.id.top_title_setting)
    private ImageView top_title_setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    private void onBack(){
        LocalMainActivity.this.finish();
    }

    @OnClick(R.id.top_title_setting)
    private void onGoSetting(){

    }
}
