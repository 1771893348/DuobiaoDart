package com.duobiao.mainframedart.network;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * heiba app 制造假JSON数据的工具类，将JSON数分别放在assert目录下，通过本工具类读取
 * 
 * @author wangfan
 *
 */
public class DummyJsonHelper {
	
	private static DummyJsonHelper sInstance = null;
	
	private DummyJsonHelper(){}
	
	public static DummyJsonHelper getInstance(){
		if(sInstance == null){
			synchronized(DummyJsonHelper.class){
				sInstance = new DummyJsonHelper();
			}
		}
		
		return sInstance;
	}
	
	/**
	 * 获取json字符串
	 * @param message
	 * @return
	 */
//	public String getJsongStr(H8HttpMessage message,String oriJson){
//		String resultJson = null;
//		
//		String path = message.path;
//		///account/mobcode/
//		
//		if(TextUtils.equals(path, DHMessage.PATH__ACCOUNT_LOGIN_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_login.txt");
//		}else if (TextUtils.equals(path, DHMessage.PATH__ACCOUNT_MOBCODE_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_mobcode.txt");
//		}else if (TextUtils.equals(path, DHMessage.PATH__ACCOUNT_MOBCODEEX_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_mobcodeex.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__ACCOUNT_REGISTER_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_register.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__ACCOUNT_MYINFO_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_myinfo.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__ACCOUNT_UPDATEINFO_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_updateinfo.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__ACCOUNT_PSWDRESET_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"account_pswdreset.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__FRIEND_FRIEND_LIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"friend_friend_list.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__CLUB_NEARBY_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"club_nearby.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__DISCOVERY_NEARBYLIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"discovery_nearbylist.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__PHOTO_LOGO_LIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"photo_logo_list.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__GROUP_MYLIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"group_mylist.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__USER_MOREINFO_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"user_more_info.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__COMMENT_LIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"comment_list.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__USER_INFOS_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"user_infos.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__FRIEND_FOCUS_LIST_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"friend_focuselist.txt");
//		}else if(TextUtils.equals(path, DHMessage.PATH__PHOTO_UPLOAD_)){
//			resultJson = getAssertString(HeibaApplication.getInstance(),"photo_upload.txt");
//		}else{
//			resultJson = oriJson;
//		}
//		
//		return resultJson;
//	}
	
	/**
	 * 获取Assert目录下面文件的字符串类容
	 * @param context
	 * @param assertFileName
	 * @return
	 */
	public String getAssertString(Context context, String assertFileName){
		StringBuffer buffer = new StringBuffer();
		AssetManager am = context.getAssets();
		try {
			InputStream is = am.open(assertFileName);
			
			String line; // 用来保存每行读取的内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			line = reader.readLine(); // 读取第一行
	        while (line != null) { // 如果 line 为空说明读完了
	            buffer.append(line); // 将读到的内容添加到 buffer 中
	            buffer.append("\n"); // 添加换行符
	            line = reader.readLine(); // 读取下一行
	        }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer.toString();
	}	
}
