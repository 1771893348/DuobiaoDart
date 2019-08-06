package com.duobiao.mainframedart.network.msg;

import android.text.TextUtils;

import com.duobiao.mainframedart.network.AbsMessage;
import ty.duobiao.dartscool.protocol.messageproxy.DHMessage;
import java.util.HashMap;


/**
 * 通过Http方式和Server沟通的消息结构
 *
 * @author wangfan
 */
public class H8HttpMessage extends AbsMessage {

    public HashMap<String, Object> multiPartData;

    /**
     * api路径 eg /account/login
     */
    public String path;

    public boolean useSSL;

    public String method;

    public String url;

    public static H8HttpMessage createForHttp(String path, HashMap<String, String> params, String method, boolean useSSL) {
        H8HttpMessage message = new H8HttpMessage();
        if (useSSL) {
            message.msgType = CONNECT_HTTPS;
        } else {
            message.msgType = CONNECT_HTTP;
        }
        message.path = path;
        if (params == null) {
            params = new HashMap<String, String>();
        }
        String verify = "verify";
        if (!TextUtils.isEmpty(verify) && !TextUtils.equals(message.path, DHMessage.PATH__ACCOUNT_LOGIN_) && !TextUtils.equals(message.path, DHMessage.PATH__SYSTEM_CHECK_VERSION) && !TextUtils.equals(message.path, DHMessage.PATH__ACCOUNT_MOBCODE_) && !TextUtils.equals(message.path, ty.duobiao.dartscool.protocol.messageproxy.DHMessage.PATH__ACCOUNT_MOBCODEEX_) && !TextUtils.equals(message.path, ty.duobiao.dartscool.protocol.messageproxy.DHMessage.PATH__ACCOUNT_PSWDRESET_)) {
            params.put(HTTP_KEY_VERIFY, verify);
        }
        params.put(HTTP_KEY_VERSION, "1.0.0");///
        message.apiParams = params;
        message.method = method;
        message.useSSL = useSSL;
        return message;
    }

    public static H8HttpMessage createForHttpDefeat(String url, HashMap<String, String> params, String method, boolean useSSL) {
        H8HttpMessage message = new H8HttpMessage();
        message.msgType = CONNECT_HTTP_DEFAUT;
        message.path = url;
        if (params == null) {
            params = new HashMap<String, String>();
        }
        message.apiParams = params;
        message.method = method;
        message.useSSL = useSSL;
        message.url = url;
        return message;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, String> getHttpParams() {
        // TODO Auto-generated method stub
        if (this.apiParams instanceof HashMap) {
            return (HashMap<String, String>) this.apiParams;
        }
        return null;
    }

    @Override
    public void excute() {

    }
}
