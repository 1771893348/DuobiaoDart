package com.duobiao.mainframedart.game.local.hightscore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.ble.activity.NewBleActivity;
import com.duobiao.mainframedart.game.base.BaseGameActivity;
import com.duobiao.mainframedart.game.bean.DartBean;
import com.duobiao.mainframedart.game.bean.PlayerBean;
import com.duobiao.mainframedart.game.bean.RoundDarts;

import java.util.ArrayList;

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
    @BindView(R.id.text_show)
    TextView text_show;
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
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
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

    @Override
    public void showResult(PlayerBean playerBean) {
        StringBuilder sb = new StringBuilder();
        sb.append(playerBean.getPlayerName());
        sb.append(":\n");
        ArrayList<RoundDarts> list = playerBean.getRounds();
        for (int i = 0;i<list.size();i++) {
            sb.append("R"+(i+1)+"  ");
            RoundDarts roundDarts = list.get(i);
            DartBean[] dartBeans = roundDarts.getRoundDarts();
            for (int j=0;j<dartBeans.length;j++){
                if (null != dartBeans[j]){
                    sb.append(dartBeans[j].getDartShow()+" ");
                }
            }
            sb.append("\n");
        }
        text_show.setText(sb);
    }


}
