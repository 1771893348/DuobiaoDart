package com.duobiao.mainframedart.network.imInterface;

/**
 * Author:Admin
 * Time:2019/8/6 10:21
 * 描述：
 */
public interface ApiCallBack {
    /**
     * UI层面和网络层的交互接口
     * @param data	从网络层返回的数据
     * @param apiPath  api路径（eg /account/login ）
     */
    public void onDataArrival(Object data,String apiPath);

    /**
     * 数据获取失败之后的回调接口（包括网络不通以及server认为访问有异常）
     * @param data
     * @param apiPath
     */
    public void onDataFailed(Object data,String apiPath);

    /**发起API访问的时候，通知UI层展示联网UI的接口*/
    public void onNetShow();

    /**API访问完成（单条和多条），通知UI层关闭联网UI的接口*/
    public void onNetDismiss();
}
