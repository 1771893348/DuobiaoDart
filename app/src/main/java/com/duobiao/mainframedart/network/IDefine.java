package com.duobiao.mainframedart.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.duobiao.mainframedart.BuildConfig;


public class IDefine {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final String searchPlaceURL = "http://api.map.baidu.com/place/v2/search";// 百度位置搜索
    public static final String searchPlaceURLText = "http://restapi.amap.com/v3/place/text";// 高德关键字搜索
    public static final String searchPlaceURLAround = "http://restapi.amap.com/v3/place/around";// 高德周边搜索
    public static final String gatewayURL = "";// 动态分配链接
    // public static final String bundleId = "taiqiu.tv.HeibaTest";//
    // ty.duobiao.dartscool 预览
    //	 public static final String bundleId = "ty.duobiao.dartscool";//
    //	// ty.duobiao.dartscoolDevelop 线上
    //  	 public static final String bundleId = "ty.duobiao.dartscoolDevelop";//
    // ty.duobiao.dartscool 上线
    public static final String bundleId = ""; //
    // 	public static final String bundleId = "ty.duobiao.DartscoolDevelop"; //测试
    public static final int versersionType =0;// 0 1 2 开发 测试 上线 // 测试
    //	public static final int versersionType =0;// 0 1 2 开发 测试 上线 // 测试
    public static final String versionCode = "";// api版本号
//    public static final String channelName = "TD_CHANNEL_ID";
    public static final String channelName = "channel_value";
    public static final String channelValue = "10000";
    public static final String HTTPS = "https://";
    public static final String HTTP = "http://";
    public static String HTTP_HOST = "";// HTTP IPP
    public static String SOCKET_HOST = "";// socket IP
    public static String HTTPS_HOST = "";// HTTPS IP
    public static String LOG_HOST = "";// LOG IP
    public static int HTTP_PORT = 8081;// http 端口
    public static int SOCKET_PORT = 8000;// socket端口
    // public static int FILEUP_PORT = 8081;// 上传文件端口
    public static int HTTPS_PORT = 8081;// https 端口
    public static int LOG_PORT = 9001;// 上传日志
    public static final int ERRORCODE_ERROR = -1;
    public static final int ERRORCODE_OK = 0;
    public static final int ERRORCODE_UPDATE = 1;
    public static final int ERRORCODE_UPDATE_ERROR = 2;
    /**
     * 重新登陆
     */
    public static final int ERRORCODE_CONFLICT0 = 10107;
    public static final int ERRORCODE_CONFLICT1 = 10042;
    public static final int ERRORCODE_CONFLICT2 = 10016;
    public static final int ERRORCODE_CONFLICT3 = 10008;
    public static final int ERRORCODE_CONFLICT4 = 10012;
    public static final int ERRORCODE_NO_RESPOND = 10004;
    public static final int ERRORCODE_NO_PERMISSION = 10014;
    public static final int ERRORCODE_MAX_NUM = 10028;
    public static final int SEND_HTTP_YES = 1;
    public static final int SEND_HTTP_AUTO = 2;

    public static String getChannelCode(Context context) {
        String code = getMetaData(context, channelName);
        if (code != null) {
            return code;
        }
        return channelValue;
    }

    public static String getHttpUrlPrefix() {
        return String.format("%s%s:%d", IDefine.HTTP, IDefine.HTTP_HOST, IDefine.HTTP_PORT);
    }


    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
        }
        return null;

    }

    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') aChar = '\t';
                    else if (aChar == 'r') aChar = '\r';
                    else if (aChar == 'n') aChar = '\n';
                    else if (aChar == 'f') aChar = '\f';
                    outBuffer.append(aChar);

                }
            } else outBuffer.append(aChar);
        }
        return outBuffer.toString();

    }
}
