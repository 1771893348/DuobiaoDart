package com.duobiao.mainframedart.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import butterknife.ButterKnife;

/**
 * Author:Admin
 * Time:2019/7/26 16:23
 * 描述：
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int viewId = getContentView();
        if (viewId > 0){
            setContentView(viewId);
            //绑定初始化ButterKnife
            ButterKnife.bind(this);
            initView();
            initListener();
        }

    }
    protected abstract  int getContentView();
    protected abstract  void initView();
    protected abstract  void initListener();
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
}
