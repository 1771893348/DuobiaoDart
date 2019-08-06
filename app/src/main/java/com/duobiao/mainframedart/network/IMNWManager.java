package com.duobiao.mainframedart.network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.duobiao.mainframedart.BuildConfig;
import com.duobiao.mainframedart.network.imInterface.ApiCallBack;
import com.duobiao.mainframedart.network.imInterface.ITaskCallBack;
import com.duobiao.mainframedart.network.msg.H8HttpMessage;
import com.duobiao.mainframedart.network.msg.H8SocketMessage;
import com.duobiao.mainframedart.network.nio.structures.BaseClient;
import com.duobiao.mainframedart.network.nio.structures.BaseMessageProcessor;
import com.duobiao.mainframedart.network.nio.structures.IConnectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class IMNWManager {

    private static final String tag = "IMNWManager";
    public IMNWOKHttpConnect httpConnect;
    public IMNWOKHttpConnect httpsConnect;
    public IMNWOKHttpConnect httpConnectDefeat;
    public IMNWSocketConnect socketConnect;
//    public AsyncImgUploadTask imgUploadTask;
    public MessageHandler messageHandler;

    private IMNWManager() {
        messageHandler = new MessageHandler(Looper.myLooper());
    }

    private static IMNWManager mInstance = null;

    public static IMNWManager getInstance() {
        if (mInstance == null) {
            synchronized (IMNWManager.class) {
                if (mInstance == null) mInstance = new IMNWManager();
            }
        }
        return mInstance;
    }

    public IMNWSocketConnect initSocketConnect() {
        if (socketConnect == null) {
            synchronized (IMNWManager.class) {
                if (socketConnect == null)
                    socketConnect = new IMNWSocketConnect(IDefine.SOCKET_HOST, IDefine.SOCKET_PORT, mConnectResultListener, mMessageProcessor);
            }
        }
        return socketConnect;
    }

    public IMNWSocketConnect getSocketConnect() {
        if (socketConnect == null || TextUtils.isEmpty(socketConnect.getHost())) {
            initSocketConnect();
        }
        return socketConnect;
    }

    public void setSocketConnect(IMNWSocketConnect socketConnect) {
        this.socketConnect = socketConnect;
    }

    public void closeSocketConnect() {
        if (socketConnect != null) {
            socketConnect.disconnect();
            socketConnect = null;
        }
    }

    public void initHttpsConnect() {
        httpsConnect = new IMNWOKHttpConnect(IDefine.HTTPS_HOST, IDefine.HTTPS_PORT);
    }

    public void initHttpConnect() {
        httpConnect = new IMNWOKHttpConnect(IDefine.HTTP_HOST, IDefine.HTTP_PORT);
    }

    public void initHttpConnectDefeat() {
        httpConnectDefeat = new IMNWOKHttpConnect();
    }

    public void sendMessage(Context context, AbsMessage message, ApiCallBack callBack) {
        switch (message.msgType) {
            case AbsMessage.CONNECT_HTTPS:
                // make sure httpConnect object exist,think java vm connect
                // object may be reccycled
                if (httpsConnect == null || TextUtils.isEmpty(httpsConnect.getHost())) {
                    synchronized (IMNWManager.class) {
                        if (httpsConnect == null) initHttpsConnect();
                    }
                }
                this.httpsConnect.sendHttpRequest(context, (H8HttpMessage) message, callBack);
                break;
            case AbsMessage.CONNECT_HTTP:
                // make sure httpConnect object exist,think java vm connect
                // object may be reccycled
                if (httpConnect == null || TextUtils.isEmpty(httpConnect.getHost())) {
                    synchronized (IMNWManager.class) {
                        if (httpConnect == null) initHttpConnect();
                    }
                }
                this.httpConnect.sendHttpRequest(context, (H8HttpMessage) message, callBack);
                break;
            case AbsMessage.CONNECT_HTTP_DEFAUT:
                // make sure httpConnect object exist,think java vm connect
                // object may be reccycled
                if (httpConnectDefeat == null) {
                    synchronized (IMNWManager.class) {
                        if (httpConnectDefeat == null) initHttpConnectDefeat();
                    }
                }
                this.httpConnectDefeat.sendHttpRequest(context, (H8HttpMessage) message, callBack);
                break;
            case AbsMessage.CONNECT_SOCKET:

                if (socketConnect == null || TextUtils.isEmpty(socketConnect.getHost())) {
                    initSocketConnect();
                }
                this.socketConnect.sendData(message);
                break;
            case AbsMessage.CONNECT_FILEUPLOAD:
//                H8FileUploadMessage h8FileuploadMsg = (H8FileUploadMessage) message;
//                String fullUrl = h8FileuploadMsg.apiPath;
//                if (!h8FileuploadMsg.apiPath.equals(DHMessage.PATH__LOG_UPLOAD)) {
//                    fullUrl = IDefine.HTTP + IDefine.HTTP_HOST + ":" + IDefine.HTTP_PORT + h8FileuploadMsg.apiPath;
//                } else {
////                    fullUrl = IDefine.HTTP + IDefine.LOG_HOST + ":" + IDefine.LOG_PORT + "/log/upload";
//                    fullUrl = IDefine.LOG_HOST + ":" + IDefine.LOG_PORT + DHMessage.PATH__LOG_UPLOAD;
//                }
//                Log.d(h8FileuploadMsg.apiPath, "---上传文件---" + fullUrl);
//                imgUploadTask = new AsyncImgUploadTask(fullUrl, 0, (HashMap) h8FileuploadMsg.apiParams, new ApiCounter(), new FileUploadTaskCallBack(context, h8FileuploadMsg.apiPath, callBack));
//                if (context != null) {
//                    if (requestMap == null) {
//                        requestMap = new HashMap<Context, List<AsyncImgUploadTask>>();
//                    }
//                    List<AsyncImgUploadTask> uploadTasks = requestMap.get(context);
//                    if (uploadTasks == null) {
//                        uploadTasks = new ArrayList<AsyncImgUploadTask>();
//                        requestMap.put(context, uploadTasks);
//                    }
//                    uploadTasks.add(imgUploadTask);
//                }
//                imgUploadTask.execute(new Void[0]);
            default:
                break;
        }
    }

    //    Handler handler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            AbsMessage message = (AbsMessage) msg.obj;
    //            getSocketConnect().sendData(message);
    //        }
    //    };
//    private Map<Context, List<AsyncImgUploadTask>> requestMap = null;

//    public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
//        if (context == null || requestMap == null) {
//            return;
//        }
//        List<AsyncImgUploadTask> requestList = requestMap.get(context);
//        if (requestList != null) {
//            for (AsyncImgUploadTask requestHandle : requestList) {
//                requestHandle.cancel(mayInterruptIfRunning);
//            }
//            requestMap.remove(context);
//        }
//    }

//    public void cancelAllRequests(Context context, ApiCallBack apiCallBack, String apiPath) {
//        if (!ApiRecord.getInstance().isApiRunning(apiCallBack, apiPath)) {
//            if (httpConnect != null) {
//                httpConnect.cancelAllRequests(context);
//            }
//            if (httpConnectDefeat != null) {
//                httpConnectDefeat.cancelAllRequests(context);
//            }
//            if (httpsConnect != null) {
//                httpsConnect.cancelAllRequests(context);
//            }
//            cancelRequests(context, true);
//            ApiRecord.getInstance().unregistAllApi(apiCallBack);
//        }
//    }

    // 取消所有apiCallBack请求
//    public void cancelAllRequests(Context context, ApiCallBack apiCallBack) {
//        if (!ApiRecord.getInstance().allApiFinish(apiCallBack)) {
//            if (httpConnect != null) {
//                httpConnect.cancelAllRequests(context);
//            }
//            if (httpConnectDefeat != null) {
//                httpConnectDefeat.cancelAllRequests(context);
//            }
//            if (httpsConnect != null) {
//                httpsConnect.cancelAllRequests(context);
//            }
//            cancelRequests(context, true);
//            ApiRecord.getInstance().unregistAllApi(apiCallBack);
//        }
//    }

//    public void cancelAllRequests(Context context) {
//        if (context == null) {
//            return;
//        }
//        if (httpConnect != null) {
//            httpConnect.cancelAllRequests(context);
//        }
//        if (httpConnectDefeat != null) {
//            httpConnectDefeat.cancelAllRequests(context);
//        }
//        if (httpsConnect != null) {
//            httpsConnect.cancelAllRequests(context);
//        }
//        cancelRequests(context, true);
//    }


    //socket链接 状态监听
    private IConnectListener mConnectResultListener = new IConnectListener() {
        @Override
        public void onConnectionSuccess() {
            //链接成功
            Log.d("Socket", "socket 链接成功--------------------");
            H8SocketMessage message = new H8SocketMessage();
            message.mark = "netWorkStatus";
            message.type = "netWorkStatus";
            message.apiParams = 1;
            Message msg = IMNWManager.getInstance().messageHandler.obtainMessage(0, message);
            IMNWManager.getInstance().messageHandler.sendMessage(msg);
//            NetHeartManager.getInstance().setSocketLink();
        }

        @Override
        public void onConnectionFailed() {
            Log.d("Socket", "socket 链接断开--------------------");
            if (socketConnect != null) {
                socketConnect.setStatus(IMNWSocketConnect.SOCKET_UNCONNECT);
            }
            H8SocketMessage message = new H8SocketMessage();
            message.mark = "netWorkStatus";
            message.type = "netWorkStatus";
            message.apiParams = 0;
            Message msg = IMNWManager.getInstance().messageHandler.obtainMessage(0, message);
            IMNWManager.getInstance().messageHandler.sendMessage(msg);
        }

    };

    //报文发送
    private BaseMessageProcessor mMessageProcessor = new BaseMessageProcessor() {
        @Override
        public void onReceiveMessages(BaseClient mClient, final LinkedList<com.duobiao.mainframedart.network.nio.structures.message.Message> mQueen) {
            Log.d("wgw_mMessageProcessor","收到报文"+mQueen.size());
            parser(mQueen);
        }
    };

    private synchronized void parser(LinkedList<com.duobiao.mainframedart.network.nio.structures.message.Message> mQueen) {
        if (BuildConfig.DEBUG) {
            Log.d("Socket", "=====================================开始=====================================");
        }
        for (int i = 0; i < mQueen.size(); i++) {
            com.duobiao.mainframedart.network.nio.structures.message.Message msg = mQueen.get(i);
            byte[] msgData = new byte[msg.length];
            System.arraycopy(msg.data, msg.offset, msgData, 0, msg.length);
            if (socketConnect != null) socketConnect.readData(msgData);
        }
        if (socketConnect != null) socketConnect.parseReceieveData();
        if (BuildConfig.DEBUG) {
            Log.d("Socket", "=====================================结束=====================================");
        }
    }

    public class MessageHandler extends Handler {

        public MessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                ((H8SocketMessage) msg.obj).excute();
            } catch (Exception e) {
                e.fillInStackTrace();
            }

        }
    }

    public class FileUploadTaskCallBack implements ITaskCallBack {
        String mApiPath = null;
        ApiCallBack mApiCallBack = null;
        private Context context;

        public FileUploadTaskCallBack(Context context, String apiPath, ApiCallBack apiCallBack) {
            mApiPath = apiPath;
            this.context = context;
            this.mApiCallBack = apiCallBack;
        }

        @Override
        public void disPatchServerData(Object data) {
//            ApiRecord.getInstance().unregistApi(mApiCallBack, mApiPath);
//            BaseBean baseBean = null;
//            try {
//                baseBean = JSON.parseObject((String) data, BaseBean.class);
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//            if (baseBean == null) {
//                baseBean = new BaseBean();
//                baseBean.setError_code(-1);
//                data = JSON.toJSONString(baseBean);
//            }
            Log.w(mApiPath, data.toString());
//            if (baseBean.getError_code() == 0) {
                mApiCallBack.onDataArrival(data, mApiPath);
//            } else {
//                mApiCallBack.onDataFailed(data, mApiPath);
//            }
        }

        @Override
        public void showCurrTaskApiUi() {
//            if (!ApiRecord.getInstance().allApiFinish(mApiCallBack)) {
//                mApiCallBack.onNetShow();
//            }
        }

        @Override
        public void hideCurrTaskApiUi() {
//            if (ApiRecord.getInstance().allApiFinish(mApiCallBack)) {
//                // 当同一个Callback对应的所有的API都已执行完毕，则自动关闭联网UI
//                mApiCallBack.onNetDismiss();
//                if (context != null && requestMap != null) {
//                    requestMap.remove(context);
//                }
//            } else {
//                if (context != null && requestMap != null) {
//                    List<AsyncImgUploadTask> uploadTasks = requestMap.get(context);
//                    if (uploadTasks != null && !uploadTasks.isEmpty()) {
//                    } else {
//                        requestMap.remove(context);
//                    }
//                }
//            }
        }
    }
}
