package com.duobiao.mainframedart.network;


import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.duobiao.mainframedart.BuildConfig;
import com.duobiao.mainframedart.network.msg.H8SocketMessage;
import com.duobiao.mainframedart.network.nio.NioClient;
import com.duobiao.mainframedart.network.nio.structures.BaseMessageProcessor;
import com.duobiao.mainframedart.network.nio.structures.IConnectListener;
import com.duobiao.mainframedart.network.nio.structures.TcpAddress;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class IMNWSocketConnect {
    private static final String TAG = "Socket1";

    public static final int SOCKET_ERROR = -1;
    public static final int SOCKET_UNCONNECT = -2;
    public static final int SOCKET_CONNECTED = 0;
    public static final int SOCKET_LOGINED = 1;
    public static final int SOCKET_CONNECTING = 2;
    private NioClient mClient = null;
    private BaseMessageProcessor mMessageProcessor;

    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    /**
     * 接受消息缓存
     */
    ByteArrayOutputStream receieveBuffer = new ByteArrayOutputStream(64 * 1024);
    /**
     * 消息组装字符串
     */
    private ByteArrayOutputStream recvString = new ByteArrayOutputStream(1024 * 4);


    /**
     * @param status
     * -1 断开 0 已连接 1 已登录 2 正在连接
     */
    private Integer status = -1;

    public IMNWSocketConnect(String host, int port, IConnectListener mConnectResultListener, BaseMessageProcessor mMessageProcessor) {
        this.mMessageProcessor = mMessageProcessor;
        mClient = new NioClient(mMessageProcessor, mConnectResultListener);
        mClient.setConnectAddress(new TcpAddress[]{new TcpAddress(host, port)});
    }


    public String getHost() {
        return "1";
    }

    public int getStatus() {
        synchronized (status) {
            return status;
        }
    }


    public void setStatus(int status) {
        synchronized (this.status) {
            this.status = status;
        }
    }

    public synchronized void connect() {
        if (!mClient.isConnected() || status == SOCKET_UNCONNECT) {
            print(TAG, "socket 建立链接——————————————————————————————");
            setStatus(SOCKET_CONNECTING);
            mClient.connect();
        }
    }

    public void disconnect() {
        print(TAG, "socket 关闭链接——————————————————————————————");
        mClient.disconnect();
        setStatus(SOCKET_UNCONNECT);
    }


    public void sendData(AbsMessage message) {
        if (BuildConfig.DEBUG) {
            print(TAG, "socket <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            print(TAG, "socket 上行消息———" + new String(((H8SocketMessage) message).getSocketData()));
            print(TAG, "socket <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<结束>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }else {
            print(TAG, "socket 上行消息———" + new String(((H8SocketMessage) message).getSocketData()));
        }
        mMessageProcessor.send(mClient, ((H8SocketMessage) message).getSocketData());
    }


    public void readData(byte[] data) {
//        long time = System.currentTimeMillis();
        if (data.length>11){
            if (data[4] == 42 || data[8] == 42 || data[11] == 42){
                receieveBuffer.reset();

            }
        }
        receieveBuffer.write(data, 0, data.length);
        try {
            String res = new String(data,"UTF-8");
            print("socket----wgw---readData:",res);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void parseReceieveData() {
        recvString.reset();
        int bodySize = 0;
        H8SocketMessage tempReceieveMessage = null;
        int buffersize = receieveBuffer.size();
        byte[] readdata = receieveBuffer.toByteArray();
        if (buffersize <= 0) {
            return;
        }
        for (int pos = 0; pos < buffersize; pos++) {
            byte datatype = readdata[pos];
            if (tempReceieveMessage == null && datatype == '\n') {
                String strHead = new String(recvString.toByteArray());
                recvString.write(datatype);
                print(TAG, "头信息:" + strHead);
                //print(TAG,"收到消息头：" + strHead);
                String[] arrHead = strHead.split("\\*");
                if (arrHead.length >= 3 && arrHead[0].length()<30) {
                    String mark = arrHead[0];
                    String type = arrHead[1];
                    bodySize = Integer.parseInt(arrHead[2]);
                    tempReceieveMessage = new H8SocketMessage();
                    tempReceieveMessage.mark = mark;
                    tempReceieveMessage.type = type;

                } else {
                    tempReceieveMessage = null;
                    recvString.reset();
                }
                continue;
            }

            if (tempReceieveMessage == null) {
                recvString.write(datatype);
                continue;
            }

            int incompleteSize = buffersize - pos;
            if (incompleteSize >= bodySize && bodySize != 0) {
                recvString.reset();
                recvString.write(readdata, pos, bodySize);
                pos += bodySize;
                String strBody = new String(recvString.toByteArray());
                if (BuildConfig.DEBUG) {

                }
                print(TAG, "下行消息" + ": pos: " + pos + "   bodySize: " + bodySize + "   type: " + tempReceieveMessage.type + "  body: " + strBody);
                try {
                    //                    Gson gson = new Gson();
                    //                    tempReceieveMessage.apiParams = gson.fromJson(strBody, Object.class);
                    tempReceieveMessage.apiParams = JSON.parse(strBody);
                    Message msg = IMNWManager.getInstance().messageHandler.obtainMessage(0, tempReceieveMessage);
                    IMNWManager.getInstance().messageHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    //报文不完整 等待下一次解析
                    Log.w(TAG, "---" + "报文不完整:");
                    //return;
                }
                recvString.reset();
                receieveBuffer.reset();
                tempReceieveMessage = null;
                if (bodySize == incompleteSize) {
                    break;
                } else {
                    pos--;
                    receieveBuffer.write(readdata, pos, incompleteSize - bodySize);
                }
            } else if (incompleteSize > 0) {
                receieveBuffer.reset();
                receieveBuffer.write(recvString.toByteArray(), 0, recvString.size());
                String str = new String(recvString.toByteArray());
                print(TAG, pos+"--重新放入头" + str);
                String[] arrHead = str.split("\\*");
                if (arrHead.length >= 3 && arrHead[0].length()<20) {
                    print(TAG, "--==="+readdata[pos]);
                    receieveBuffer.write(readdata, pos, incompleteSize);
                    if (BuildConfig.DEBUG) {
                        String string = new String(receieveBuffer.toByteArray());
                        print(TAG, "--遇到不完整包" + string);
                    }
                    
                }else {
                    receieveBuffer.reset();
                }

                recvString.reset();
                break;
            } else {
                receieveBuffer.reset();
            }

        }
    }

    private void print(String tag, String msg){
        Log.d("wgw_"+tag,msg);
    }
}
