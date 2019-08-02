package com.duobiao.mainframedart.game.local.hightscore;

import android.content.Intent;
import android.widget.ImageView;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.ble.activity.NewBleActivity;
import com.duobiao.mainframedart.game.base.BaseGameActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:Admin
 * Time:2019/8/2 10:40
 * 描述：
 */
public class HightGameActivity extends BaseGameActivity implements HightGameContract.view<HightGameContract.presenter>{
    @BindView(R.id.top_title_setting)
    ImageView top_title_setting;
    HightGameContract.presenter mPresenter;
    @Override
    protected int getContentView() {
        return R.layout.activity_hight_game;
    }

    @Override
    protected void initView() {
        HightGamePresenter.attachView(this,this);
        mPresenter.onCreate();
    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.top_title_setting)
    public void bleOnClick(){
        Intent intent = new Intent();
        intent.setClass(HightGameActivity.this, NewBleActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPresenter(HightGameContract.presenter presenter) {
        mPresenter = presenter;
    }
}
