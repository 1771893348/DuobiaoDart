package com.duobiao.mainframedart.game.local;

import android.content.Intent;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.base.BaseActivity;
import com.duobiao.mainframedart.game.local.hightscore.HightGameActivity;

import butterknife.OnClick;

/**
 * Author:Admin
 * Time:2019/8/1 15:13
 * 描述：
 */
public class LocalSeatActivity extends BaseActivity {


    @Override
    protected int getContentView() {
        return R.layout.activity_local_seat;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @OnClick(R.id.btn_go_game)
    public void goPlayGame(){
        Intent intent = new Intent();
        intent.setClass(this, HightGameActivity.class);
        startActivity(intent);
    }

}
