package com.duobiao.mainframedart.network;

import android.os.Bundle;
import android.util.Log;

import com.duobiao.mainframedart.network.msg.H8SocketMessage;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class IMNWProxy {

    public static HashMap<String, IMNWProxy> proxys = new HashMap<String, IMNWProxy>();

    protected IMNWProxy() {

    }

    //	private static IMNWProxy mInstance = null;
    //
    //	public static IMNWProxy getInstance() {
    //		if (mInstance == null) {
    //			mInstance = new IMNWProxy();
    //		}
    //		return mInstance;
    //	}

    public void parseMessage(H8SocketMessage message) {
        StringBuilder sb = new StringBuilder(message.type);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "parseType");
        String strTypeMethod = sb.toString();
        try {

            Method typeMethod = this.getClass().getMethod(strTypeMethod, Object.class);
            if (typeMethod != null) {
                typeMethod.setAccessible(true);
                typeMethod.invoke(this, message.apiParams);
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Socket", "NoSuchMethodException  ： " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Socket", "IllegalArgumentException  ： " + e.getMessage());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Socket", "IllegalAccessException  ： " + e.getMessage());
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Socket", "InvocationTargetException  ： " + e.getMessage());
            Throwable t = e.getTargetException();// 获取目标异常
            t.printStackTrace();
        } catch (Exception e) {
            e.fillInStackTrace();
            Log.i("Socket", "Exception  ： " + e.getMessage());
        }
    }

    public JSONObject processJsonData(Object data) {
        JSONObject json = null;
        try {
            json = new JSONObject((String) data);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //new AlertDialog.Builder(HeibaApplication.getInstance()).setTitle("错误").setMessage("解析HttpJSON出错").show();
        }
        return json;
    }

    public void processErrorCode(int errorcode, String notiName) {
        // TODO Auto-generated method stub
        this.processErrorCode(errorcode, notiName, null);
    }

    public void processErrorCode(int errorcode, String notiName, Bundle userInfo) {
        // TODO Auto-generated method stub
//        Bundle errorInfo = new Bundle();
//        errorInfo.putInt(BroadCastUtil.BUNDLE_ERROR_CODE, errorcode);
//        //		errorInfo.putString(key, "");
//        BroadCastUtil.sendBroadcase(notiName, errorInfo, userInfo);
        Log.d("Message receive error", String.valueOf(errorcode));
    }
}
