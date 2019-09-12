package com.duobiao.mainframedart.game.local.hightscore;

import android.app.Activity;

import com.clj.fastble.data.BleDevice;
import com.duobiao.mainframedart.ble.bean.DartsTable;
import com.duobiao.mainframedart.ble.kit.BluetoothUtil;
import com.duobiao.mainframedart.ble.kit.NewBluetoothLe;
import com.duobiao.mainframedart.game.bean.DartBean;
import com.duobiao.mainframedart.game.bean.PlayerBean;
import com.duobiao.mainframedart.game.bean.RoundDarts;
import com.duobiao.mainframedart.game.manager.LocalGameManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Author:Admin
 * Time:2019/8/2 10:44
 * 描述：
 */
public class HightGamePresenter implements HightGameContract.presenter, NewBluetoothLe.BleStatusListener,NewBluetoothLe.BleDataListener {
    private PlayerBean[] playerBeans;
    private int currentPlayerIndex = 0;
    private int currentRoundIndex = 0;
    private int currentDartIndex = 0;
    private RoundDarts currentRoundDarts;
    private Activity mActivity;
    private static HightGamePresenter gameHightPresent;
    private static HightGameContract.view mIView;
    private DartsTable mDartsTable;
    HashMap<String, String> ScoreDict;//扇区对应表
    ArrayList<DartBean> dartBeans;
    public HightGamePresenter(Activity activity){
        this.mActivity = activity;
    }
    public static void attachView(Activity activity,HightGameContract.view iView){
        if (null == gameHightPresent){
            gameHightPresent = new HightGamePresenter(activity);
        }
        mIView = iView;

        iView.setPresenter(gameHightPresent);
    }

    @Override
    public void onCreate() {
        playerBeans = new PlayerBean[2];
        PlayerBean playerBean = new PlayerBean();
        playerBean.setPlayerName("wgw1");
        playerBeans[0] = playerBean;
        PlayerBean playerBean1 = new PlayerBean();
        playerBean1.setPlayerName("wgw2");
        playerBeans[1] = playerBean1;
        NewBluetoothLe.getInstance(mActivity).addBleStatusListener(this);
        if (NewBluetoothLe.getInstance(mActivity).isConnect()) {
            onConnected(null);
        } else {
            onDisConnected(null);
        }
        mDartsTable = BluetoothUtil.getDartsTable("DuobiaoDart");
        if (mDartsTable != null){
            ScoreDict = mDartsTable.getScoreDict();
        }
        dartBeans = new ArrayList<>();
        for (String key:ScoreDict.keySet()) {
            if (null != key && !key.equals("")){
                dartBeans.add(LocalGameManager.createFromByte(key));
            }
        }
        mIView.getDartBeans(dartBeans);
        currentRoundDarts = new RoundDarts();
    }

    @Override
    public void onResume() {
        NewBluetoothLe.getInstance(mActivity).addBleDataListener(this);
    }

    @Override
    public void onPause() {
        NewBluetoothLe.getInstance(mActivity).removeBleDataListener(this);
    }

    @Override
    public void onDestroy() {
            NewBluetoothLe.getInstance(mActivity).removeBleStatusListener(this);
    }

    @Override
    public void getBluetoothData(String message) {

        DartBean dartBean = LocalGameManager.createFromByte(message);
        if (currentDartIndex>=3){
            currentDartIndex = 0;
            if (currentPlayerIndex >=playerBeans.length-1){
                currentRoundIndex++;
                currentPlayerIndex = 0;
            }else {
                currentPlayerIndex++;
            }
        }else {

        }
        if (currentDartIndex == 0){
            currentRoundDarts = new RoundDarts();
            playerBeans[currentPlayerIndex].addRound(currentRoundDarts);
        }
        currentRoundDarts.getRoundDarts()[currentDartIndex] = dartBean;
        currentDartIndex++;
        mIView.showResult(playerBeans[currentPlayerIndex]);
        mIView.blueMessage(message);
    }

    @Override
    public void onConnected(BleDevice device) {
        NewBluetoothLe.getInstance(mActivity).addBleDataListener(this);
    }

    @Override
    public void onDisConnected(BleDevice device) {
        NewBluetoothLe.getInstance(mActivity).removeBleDataListener(this);
    }
}
