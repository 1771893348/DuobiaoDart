package com.duobiao.mainframedart.network.okhttp;

import okhttp3.Response;

/**
 * Author:Admin
 * Time:2019/9/4 14:54
 * 描述：
 */
public interface NetCallBack {
    void onSucceed(Response response);
    void onFailure(String msg);
}
