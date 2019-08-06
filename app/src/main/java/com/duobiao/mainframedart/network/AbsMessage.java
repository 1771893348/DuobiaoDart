package com.duobiao.mainframedart.network;


/**
 * 抽象的消息
 * @author wangfan
 *
 */
public abstract class AbsMessage {
	/**
	 * 消息的数据类型，Http还是Socket
	 */
	public int msgType;
	public static final int CONNECT_HTTPS = 0;
	public static final int CONNECT_HTTP = 1;
	public static final int CONNECT_SOCKET = 2;
	public static final int CONNECT_FILEUPLOAD = 3;
	public static final int CONNECT_HTTP_DEFAUT=4;
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	
	public static final String HTTP_KEY_VERIFY = "_verify";
	public static final String HTTP_KEY_VERSION="_version";
	/**
	 * data 是Http返回和Socket回调公用的数据对象
	 * 通过Socket将数据发送到server之后，Server通过回写的方法配合Java的反射 调用 parseTypeXxx 方法。
	 * data 就是被调用的parseTypeXxx的参数
	 * 
	 * Message 带的数据（可能是 Map<String,String>）
	 */
	public Object apiParams;
	
	public abstract void excute();
}
