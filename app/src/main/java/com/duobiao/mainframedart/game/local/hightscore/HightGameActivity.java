package com.duobiao.mainframedart.game.local.hightscore;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duobiao.mainframedart.R;
import com.duobiao.mainframedart.ble.activity.NewBleActivity;
import com.duobiao.mainframedart.game.adapter.DemoRecyclerAdapter;
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
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    HightGameContract.presenter mPresenter;
    ArrayList<DartBean> dartBeans;
    DemoRecyclerAdapter demoRecyclerAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_hight_game;
    }

    @Override
    protected void initView() {
        HightGamePresenter.attachView(this,this);
        dartBeans = new ArrayList<>();
        demoRecyclerAdapter = new DemoRecyclerAdapter(dartBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setAdapter(demoRecyclerAdapter);
//        mPresenter.onCreate();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPresenter.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mPresenter.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPresenter.onResume();
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
        getLifecycle().addObserver(presenter);
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

    @Override
    public void blueMessage(String msg) {
        for (int i = 0;i<dartBeans.size();i++) {
            DartBean dart = dartBeans.get(i);
            if (dart.getDartBleString().equals(msg)){
                dart.setEffective(true);
            }
        }
        demoRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDartBeans(ArrayList<DartBean> list) {
        if (null != list && list.size()>0){
            dartBeans.clear();
            dartBeans.addAll(list);
        }
        demoRecyclerAdapter.notifyDataSetChanged();

    }


}
