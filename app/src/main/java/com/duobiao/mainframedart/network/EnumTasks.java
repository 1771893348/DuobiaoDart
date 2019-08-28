package com.duobiao.mainframedart.network;

import android.content.Context;

import com.duobiao.mainframedart.network.imInterface.ApiCallBack;
import com.duobiao.mainframedart.network.msg.H8FileUploadMessage;
import com.duobiao.mainframedart.network.msg.H8HttpMessage;

import java.util.HashMap;

/**
 * API枚举集合体，负责创建Http消息
 *
 * @author wangfan
 *         位置搜索http://api.map.baidu.com/place/v2/search?output=json&page_num
 *         =0&ak
 *         =gBuOT3LsnEiQgGDpVzahKa3x&location=39.993469%2C116.470038&radius=
 *         2000&query=%E5%B0%8F%E5%8C%BA&page_size=100
 */
public enum EnumTasks {

    ///////wgw
    UPLOAD_VIDEO_DONE("", "", null);

    private String mApi; // api path
    private String mMethod; // post or get
    private Class<?> mClass; // javabeen used to parse json

    private EnumTasks(String api, String method, Class<?> cla) {
        this.mApi = api;
        this.mMethod = method;
        this.mClass = cla;
    }


    /**
     * 创建普通Http 消息
     *
     * @param params
     * @return
     */
    public void newTaskMessageNor(Context context, HashMap<String, String> params, boolean isHttps,
                                  ApiCallBack callBack) {
        H8HttpMessage h8HttpMsg = H8HttpMessage.createForHttpDefeat(mApi,
                params, mMethod, isHttps);
        IMNWManager.getInstance().sendMessage(context, h8HttpMsg, callBack);
    }

    public void newTaskMessageNor(Context context, HashMap<String, String> params,
                                  ApiCallBack callBack) {
        newTaskMessageNor(context, params, false, callBack);
    }

    /**
     * 创建Http 文件上传消息
     *
     * @param apiParams
     * @return
     */
    public void newFileUplodTaskMessage(Context context, HashMap apiParams, ApiCallBack callBack) {
        H8FileUploadMessage h8HttpFileUploadMsg = H8FileUploadMessage.createForHttp(mApi, apiParams, mMethod);
        IMNWManager.getInstance().sendMessage(context, h8HttpFileUploadMsg, callBack);
    }

}
