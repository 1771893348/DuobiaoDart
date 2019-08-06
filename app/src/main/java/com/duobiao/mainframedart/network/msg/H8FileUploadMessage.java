package com.duobiao.mainframedart.network.msg;

import android.text.TextUtils;

import com.duobiao.mainframedart.network.AbsMessage;

import java.util.HashMap;


/**
 * 文件上传的消息结构体
 *
 * @author wangfan
 */
public class H8FileUploadMessage extends AbsMessage {

    /**
     * api路径 eg /account/login
     */
    public String apiPath;

    public String postOrGet;

    public Object fileParams;

    public static H8FileUploadMessage createForHttp(String path,
                                                    HashMap apiParams, String method) {
        H8FileUploadMessage message = new H8FileUploadMessage();
        message.msgType = CONNECT_FILEUPLOAD;
        message.apiPath = path;
        if (apiParams == null) {
            apiParams = new HashMap();
        }
//        if (!path.equals("/log/upload")) {
//            String verify = LoginUtil.getInstance().getVerify();
//            if (!TextUtils.isEmpty(verify))
//                apiParams.put(HTTP_KEY_VERIFY, verify);
//            apiParams.put(HTTP_KEY_VERSION, IDefine.versionCode);
//        }
        message.apiParams = apiParams;
        message.postOrGet = method;

        return message;
    }

    @Override
    public void excute() {
    }
}
