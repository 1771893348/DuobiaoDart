package com.duobiao.mainframedart.network;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.duobiao.mainframedart.network.imInterface.ApiCallBack;
import com.duobiao.mainframedart.network.msg.H8HttpMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Looper.getMainLooper;

public class IMNWOKHttpConnect {
    public OkHttpClient client = null;
    public String host = null;
    public int port = 80;
    Handler mainHandler = new Handler(getMainLooper());
    public IMNWOKHttpConnect() {
        this.client = new OkHttpClient();
    }

    public IMNWOKHttpConnect(String host, int port) {
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        this.port = port;
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void sendHttpRequest(Context context, final H8HttpMessage message, final ApiCallBack listener) {
        // step 1 get full url
        String fullUrl = null;
        if (TextUtils.isEmpty(message.url)) {
            if (message.useSSL) {
                fullUrl = IDefine.HTTPS + host + ":" + IDefine.HTTPS_PORT + message.path;
            } else {
                fullUrl = IDefine.HTTP + host + ":" + IDefine.HTTP_PORT + message.path;
            }
        } else {
            fullUrl = message.url;
        }
        if (!fullUrl.endsWith("/log/upload")) {
            Log.d("sendHttpRequest", fullUrl + "?" + message.getHttpParams());
        } else {
            fullUrl = IDefine.HTTP + IDefine.LOG_HOST + ":" + IDefine.LOG_PORT + "/log/upload";
            Log.d("sendHttpRequest", fullUrl);
        }
        // step 2 : some apis use https way not http (/account/login/ 和
        // /account/register/ 和 /account/pswdreset/ 使用https方式 端口为 443)
        if (message.useSSL) {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{(X509TrustManager)trustStore}, null);
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager)trustStore)
                        .build();
            } catch (Exception e) {
            }
        }
        // step 3 do post/get
        if (message.method.equals(AbsMessage.METHOD_POST)) {
            listener.onNetShow();
            FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
            HashMap<String, String> mapParam = message.getHttpParams();
            for (String key:mapParam.keySet()) {
                formBody.add(key,mapParam.get(key));
            }
            Request request = new Request.Builder()//创建Request 对象。
                    .url(fullUrl)
                    .post(formBody.build())//传递请求体
                    .build();
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    String arg2 = call.toString();
                    Log.d("sendHttpRequest_get:", message.path + " onFailure: " + arg2+"==e="+e.toString());
                    if (listener != null) {

                        String finalArg = arg2;
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onDataFailed(finalArg, message.path);
                                listener.onNetDismiss();
                            }
                        });

                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseStr = response.body().string();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dealJsonData(message, listener, responseStr);
                        }
                    });


                }
            });
        } else {
            //构造Request对象
            //采用建造者模式，链式调用指明进行Get请求,传入Get的请求地址
            HashMap<String, String> paramsMap = message.getHttpParams();
            StringBuilder tempParams = new StringBuilder();
            try {
                //处理参数
                int pos = 0;
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    //对参数进行URLEncoder
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
                //补全请求地址
                String requestUrl = String.format("%s?%s", fullUrl, tempParams.toString());
                Request request = new Request.Builder().get().url(requestUrl).build();
                Call call = client.newCall(request);
                listener.onNetShow();
                //异步调用并设置回调函数
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        String responseStr = call.toString();
                        Log.d("sendHttpRequest_get:", message.path + " onFailure: " + responseStr+"==e="+e.toString());
                        if (listener != null) {
//                            Log.w(message.method + " " + message.path + " ", (String) arg2);
                            String finalResponseStr = responseStr;
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onDataFailed(finalResponseStr, message.path);
                                    listener.onNetDismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String responseStr = response.body().string();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dealJsonData(message, listener, responseStr);
                            }
                        });
//                        dealJsonData(message, listener, responseStr);
                    }
                });
            } catch (Exception e) {
                if (listener != null) {
                    String finalResponseStr = "";
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onDataFailed(finalResponseStr, message.path);
                            listener.onNetDismiss();
                        }
                    });

                }
            }

        }
    }

    /**
     * 处理从server返回的JSON数据
     * http  异地登录
     *
     * @param message
     * @param listener
     * @param arg2
     */
    private void dealJsonData(final H8HttpMessage message, final ApiCallBack listener, String arg2) {

        if (listener != null) {
//            BaseBean baseBean = null;
//            try {
//                //                baseBean = JSON.parseObject((String) arg2, BaseBean.class);
//                Gson gson = new Gson();
//                baseBean = gson.fromJson(arg2, BaseBean.class);
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//            if (baseBean == null) {
//                baseBean = new BaseBean();
//                baseBean.setError_code(-1);
//                arg2 = JSON.toJSONString(baseBean);
//            }
//            if (baseBean.getError_code() == IDefine.ERRORCODE_OK) {
//                listener.onDataArrival(arg2, message.path);
//            } else if ((baseBean.getError_code() == IDefine.ERRORCODE_CONFLICT0 || baseBean.getError_code() == IDefine.ERRORCODE_CONFLICT1 || baseBean.getError_code() == IDefine.ERRORCODE_CONFLICT2 || baseBean.getError_code() == IDefine.ERRORCODE_CONFLICT3)) {
//                HeibaApplication.getInstance().shotOff(true);
//            } else if (baseBean.getError_code() == IDefine.ERRORCODE_CONFLICT4) {
//                //未登录
//                HeibaApplication.getInstance().shotOff(false);
//            } else {
                listener.onDataFailed(arg2, message.path);
//            }
//            listener.onNetDismiss();
        }
    }

    public void cancelAllRequests(Context context) {
//        client.cancelRequests(context, true);
    }

    public void cancelAllRequests() {
//        client.cancelAllRequests(true);
    }


    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (java.security.cert.Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }
    /**
     * 添加password
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
