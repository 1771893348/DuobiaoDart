package com.duobiao.mainframedart.network.msg;

import android.text.TextUtils;
import android.util.Log;

import com.duobiao.mainframedart.network.AbsMessage;
import com.duobiao.mainframedart.network.IMNWManager;
import com.duobiao.mainframedart.network.IMNWProxy;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * SocketMessage 对应的结构体
 * 
 *
 */
public class H8SocketMessage extends AbsMessage {
	public static final String SOCKET_TYPE = "type";
	public static final String SOCKET_MARK = "mark";
	public String mark;
	/**
	 * 通过Socket将数据发送到server之后，Server通过回写的方法配合Java的反射 调用 parseTypeXxx 方法。(Xxx
	 * 就是这个type对应的String)
	 */
	public String type;
	/**
	 * 创建Socket消息体
	 * 
	 * @param mark
	 * @param type
	 * @return
	 */
	public static H8SocketMessage createSocketMessage(String mark, String type) {
		H8SocketMessage message = new H8SocketMessage();
		message.msgType = CONNECT_SOCKET;
		message.mark = mark;
		message.type = type;
		return message;
	}

	/**
	 * socket data
	 * 
	 * @return
	 */
	public byte[] getSocketData() {
//		synchronized (this){
			if (this.apiParams instanceof byte[]) {
				return (byte[]) this.apiParams;
			}
			return null;
//		}
	}

	/**
	 * 通过Socket将数据输出到IMServer
	 */
	public void excute() {
//		synchronized (this){
			if (TextUtils.isEmpty(mark)) {
				return;
			}
			StringBuilder sb = new StringBuilder(this.mark);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			sb.append("MessageProxy");
			// sb.insert(0, "ty.duobiao.dartscool.messageproxy.");
			sb.insert(0, "ty.duobiao.dartscool.protocol.messageproxy.");
			String strMarkClass = sb.toString();
			IMNWProxy proxy = IMNWProxy.proxys.get(strMarkClass);
			if (proxy != null) {
				try {
					proxy.parseMessage(this);
				}catch (Exception e){
					e.printStackTrace();
					Log.i("Socket",e.getMessage());
				}

			}
//		}

	}

	/**
	 * send out socket message
	 * 
	 * @param info
	 * @param keyRouteId
	 */
	public void send(HashMap<String, Object> info, String keyRouteId) {

			if (this.msgType == CONNECT_SOCKET) {
				if (info == null) {
					info = new HashMap<String, Object>();
				}
				String routeId = (String) info.get(keyRouteId);
				if (keyRouteId == null||routeId== null) {
					routeId = "0";
				}
				JSONObject jsonBody = new JSONObject(info);
				String strBody = jsonBody.toString();
				byte[] bytesBody = strBody.getBytes();
				StringBuilder sbHead = new StringBuilder(this.mark);
		        sbHead.append("*").append(this.type).append("*").append(routeId)
				.append("*").append(bytesBody.length).append("\n");
//				sbHead.append("*").append(this.type).append("*").append(0)
//						.append("*").append(bytesBody.length).append("\n");
				byte[] bytesHead = sbHead.toString().getBytes();
				if(!this.type.equals("beat")){
					byte[] bytesData = new byte[bytesHead.length + bytesBody.length];
					System.arraycopy(bytesHead, 0, bytesData, 0, bytesHead.length);
					System.arraycopy(bytesBody, 0, bytesData, bytesHead.length,		bytesBody.length);
					this.apiParams = bytesData;
				}else {
				  sbHead = new StringBuilder(this.mark);
					sbHead.append("*").append(this.type).append("*").append(routeId)
							.append("*").append(0).append("\n");
					bytesHead = sbHead.toString().getBytes();
					this.apiParams = bytesHead;
				}



			}
			IMNWManager.getInstance().sendMessage(null,this, null);
		}


}
