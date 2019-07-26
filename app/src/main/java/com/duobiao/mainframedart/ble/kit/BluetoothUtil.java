package com.duobiao.mainframedart.ble.kit;

import android.content.Context;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duobiao.mainframedart.ble.bean.DartsTable;
import com.duobiao.mainframedart.util.PrefUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**
 * @author lpmo
 * 
 */
public class BluetoothUtil {

	private static BluetoothUtil Util = null;

	private boolean isConnect;
	private String mDeviceAddress;

	private static Context contexts;
	private BluetoothUtil() {
		super();
	}

	public static BluetoothUtil getInstance(Context context) {
		if (Util == null) {
			Util = new BluetoothUtil();

		}
		 contexts =context;
		return Util;
	}



	public boolean isConnect() {

		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public String getmDeviceAddress() {

	    String address=	PrefUtils.getString(contexts.getApplicationContext(),"blue_address","");
		if(!address.isEmpty()){
			return address;
		}else {
			return mDeviceAddress;
		}

	}

	public void setmDeviceAddress(String mDeviceAddress) {
		this.mDeviceAddress = mDeviceAddress;
	}

	public String getRandomKey(){
		Set<String> keySet = blueKeyMap.keySet();
		int index = new Random().nextInt() % keySet.size();
		int i = 0;
		if (index < 0){
			index = -index;
		}
		String randomKey = "";
		for (String key : keySet){
			if (i == index){
				randomKey = key;
			}
			i++;
		}
		return randomKey;
	}



	/**	 * 存放所有已知设备 信号对应数据	 */
	private static Map<String, String> blueKeyMap = new HashMap<String, String>();
	/**	 * 存放所有已知设备名称	 */
	private static Set<String> blueDeviceNames = new HashSet<String>();
	private static Map<String, DartsTable> dartsTables = new HashMap<String, DartsTable>();
	private static String mDartTargetName="";
	public static String getmDartTargetName(){
		return mDartTargetName;
	}
	public static String getAreaMarkOfKey(String key){
		return blueKeyMap.get(key);
	}

	public static String turnBleName(String bleName) {
		if (bleName == null) {
			bleName = "";
		}
		String nameTmp;
		switch (bleName) {
			case "DuoBiaoDarts":
				nameTmp = "DuoBiaoDarts";
				break;
			case "duobiao_h_01_01":
				nameTmp = "DB-6";
				break;
			case "DuoBiao_h_01_01":
				nameTmp = "DB-6";
				break;
			case "EAGLEDART":
				nameTmp = "DB-1";
				break;
			case "DuoBiao_h01_01":
				nameTmp = "DB-6";
				break;
			default:
				nameTmp = "";
				break;
		}
		return nameTmp;
	}

	/**
	 *  根据蓝牙名字获取serviceID和UUID
	 * @param dartTargetName  文件表中录入的蓝牙名字
	 */
	public static void setBlueConfig(String dartTargetName){
//		String jsonDartName = turnBleName(dartTargetName);
		if (null == dartTargetName||dartTargetName.equals("")){
			return;
		}
		DartsTable dartsTable = dartsTables.get(dartTargetName);
		if (null == dartsTable){
			return;
		}
		mDartTargetName = dartTargetName;
		SampleGattAttributes.CLIENT_DUOBIAO_SOCKET_CHARACTERISTIC_CONFIG = dartsTable.getServiceUUID();
		SampleGattAttributes.CLIENT_DUOBIAO_GATT_CHARACTERISTIC_CONFIG = dartsTable.getCharacteristicUUID().get(0);
	}

	/**
	 * 初始化已经知道的蓝牙设备数据
	 * @param configInfo
	 */
	public static void initBlueConfig(String configInfo) {
		JSONObject all = JSONObject.parseObject(configInfo);
		JSONArray array = all.getJSONArray("arry");
		String targetName = "";
		for (int i = 0; i < array.size(); i++) {
			JSONObject device = array.getJSONObject(i);
			DartsTable dartsTable = new DartsTable();
			for (String key : device.keySet()) {
				blueDeviceNames.add(key);
				targetName = key;
				JSONObject deviceInfo = device.getJSONObject(key);
				String serviceUUID = deviceInfo.getString("ServiceUUID");
				JSONArray characteristicUUID = deviceInfo.getJSONArray("CharacteristicUUID");
				JSONObject scoreDict = deviceInfo.getJSONObject("ScoreDict");
				HashMap<String, String> scoreDictMap = new HashMap<>();
				for (String scoreKey : scoreDict.keySet()) {
					String areaMark = scoreDict.getString(scoreKey);
					String changeMark = blueKeyMap.get(areaMark);
					if (null == changeMark) {
						if (!blueKeyMap.containsKey(scoreKey)){
							blueKeyMap.put(scoreKey, areaMark);
						}
						scoreDictMap.put(scoreKey, areaMark);
					} else {
						if (!blueKeyMap.containsKey(scoreKey)){
							blueKeyMap.put(scoreKey, changeMark);
						}
						scoreDictMap.put(scoreKey, changeMark);
					}

				}
				ArrayList<String> uuid = new ArrayList<>();
				for (int j=0;j< characteristicUUID.size();j++) {
					uuid.add((String) characteristicUUID.get(j));
				}
				dartsTable.setDartTargetName(targetName);
				dartsTable.setServiceUUID(serviceUUID);
				dartsTable.setCharacteristicUUID(uuid);
				dartsTable.setScoreDict(scoreDictMap);
			}
			if (!targetName.equals("")){
				dartsTables.put(targetName,dartsTable);
			}
		}

	}
	/**
	 * 初始化已经知道的蓝牙设备数据
	 * @param configInfo
	 */
	public static void initBlueConfig1(String configInfo){
		JSONObject all = JSONObject.parseObject(configInfo);
		JSONArray array = all.getJSONArray("arry");
		for (int i = 0; i < array.size(); i ++){
			JSONObject device = array.getJSONObject(i);
			for (String key : device.keySet()){
				blueDeviceNames.add(key);
				JSONObject deviceInfo = device.getJSONObject(key);
				String serviceUUID = deviceInfo.getString("ServiceUUID");
				JSONArray characteristicUUID = deviceInfo.getJSONArray("CharacteristicUUID");
				JSONObject scoreDict = deviceInfo.getJSONObject("ScoreDict");
				for (String scoreKey : scoreDict.keySet()){
					String areaMark = scoreDict.getString(scoreKey);
					String changeMark = blueKeyMap.get(areaMark);
					if (null == changeMark){
						blueKeyMap.put(scoreKey,areaMark);
					}else{
						blueKeyMap.put(scoreKey,changeMark);
					}

				}
			}
		}

		// TODO: 2018/6/21  debug info
//		for (String key : blueKeyMap.keySet()){
//			Log.i("BluetoothUtil" , key +":"+ blueKeyMap.get(key));
//		}
	}
}
