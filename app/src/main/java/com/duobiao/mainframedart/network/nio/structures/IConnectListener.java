package com.duobiao.mainframedart.network.nio.structures;

/**
 * description  :   连接状态回调
 */

public interface IConnectListener {

    void onConnectionSuccess();

    void onConnectionFailed();
}
