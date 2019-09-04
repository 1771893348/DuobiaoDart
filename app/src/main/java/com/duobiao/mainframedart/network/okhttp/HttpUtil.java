package com.duobiao.mainframedart.network.okhttp;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author:Admin
 * Time:2019/9/4 15:00
 * 描述：https://blog.csdn.net/qq_33780359/article/details/86507343
 */
public class HttpUtil {
    private static HttpUtil instance;
    private static OkHttpClient mOkHttpClient;
    public static synchronized HttpUtil getInstance(){
        if (instance == null){
            synchronized (HttpUtil.class){
                if (instance == null){
                    instance = new HttpUtil();
                }
            }
        }
        return instance;
    }

    public HttpUtil(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(10,TimeUnit.SECONDS);
        builder.writeTimeout(60,TimeUnit.SECONDS);
//        builder.sslSocketFactory()
//        builder.hostnameVerifier()
        mOkHttpClient = builder.build();
    }
    /**
     * On async get. 异步GET请求
     *
     * @param netCallBack the net call back
     */
    private void onAsyncGET(String url,final NetCallBack netCallBack){
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netCallBack.onFailure(e.getMessage().toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                netCallBack.onSucceed(response);
            }
        });
    }
    /**
     * 同步GET请求
     *
     * @param netCallBack
     * @throws IOException
     */
    private void onSyncGet(String url,NetCallBack netCallBack){
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            netCallBack.onSucceed(response);
        } catch (IOException e) {
            e.printStackTrace();
            netCallBack.onFailure(e.getMessage().toString());
        }

    }

    /**
     * 设置bodyParams
     * @param bodyParams
     * @return RequestBody
     */
    private RequestBody getBodyParams(Map<String,String> bodyParams){
        RequestBody body = null;
        FormBody.Builder formBody = new FormBody.Builder();
        if (bodyParams != null){
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()){
                key = iterator.next().toString();
                formBody.add(key,bodyParams.get(key));
            }
        }
        return formBody.build();
    }

    /**
     * 同步POST请求 无 headers
     * @param url
     * @param bodyParams
     * @return
     */
    private Response onSyncPOST_Body(String url, Map<String,String> bodyParams) throws IOException {
        RequestBody body = getBodyParams(bodyParams);
        Request.Builder builder = new Request.Builder();
        Request request = builder.post(body)
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }
//    private Response onSyncPOST_BodyAndHeader(String url,Map<String ,String> map){
//
//    }
}
