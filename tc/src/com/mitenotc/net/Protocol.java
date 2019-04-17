package com.mitenotc.net;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

import com.mitenotc.exception.ProtocolException;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

/*protocal 用的是 utf-8的编码*/
public class Protocol {

	private static Protocol instance = new Protocol();
	public static String login_required_cmds;
	private Protocol() {
	}

	public static Protocol getInstance() {
		return instance;
	};

	private String CMD;// 命令号 + 具体到协议的协议号里再封装
	private String VER;// APP版本号 1 o
	private String MSG;// 加密消息体 + 发送协议的时候,加密之后再封装
	private String USR;// 手机号码:用户账号 1 用户输入
	private String PSW;//用于加密
	private String SND;// SESSIONID + 登入后返回的参数中获得
	private String DES;
	private String A;// 屏幕分辨率 1 o
	private String B;// 设备名称 1 o
	private String C;// 设备序号 IME+SIM 1 -
	private String D;// 操作系统名称和版本 1 o
	private String E;// 设备IP地址 + 监听
	private String F = "ANDROID";// 操作来源 常量,android
	private String G="10000001";
	private String H= AppUtil.getMetaData(MyApp.context).getString("UMENG_CHANNEL");//"CH000";//渠道统计同时要添加到友盟统计上

	/**
	 * 获取 协议封装好的 hashmap,如果协议的 CMD == null || VER == null || MSG == null
	 * 将会抛出运行时异常
	 * @return
	 */
	public JSONObject getProtocol(){
		if(AppUtil.isEmpty(VER)){
			VER = AppUtil.getVersion(MyApp.context);
		}
		if(AppUtil.isEmpty(USR)){// 成功登陆过用的的USR 为主
			USR = SPUtil.getString(MyApp.res.getString(R.string.USR));
		}
		if(AppUtil.isEmpty(SND)){// 成功登陆过用的的USR 为主
			SND = SPUtil.getString(MyApp.res.getString(R.string.SND));
		}
		
		JSONObject protocol = new JSONObject();
		DES = "0";
		if (AppUtil.isEmpty(CMD) || AppUtil.isEmpty(VER) || AppUtil.isEmpty(DES)) {// 任何协议中都必须包含字段CMD、VER和MSG
			throw new RuntimeException("CMD or VER or DES is null");//该异常主要给 网络层发送该协议时 检验
		}

		int cmd = Integer.parseInt(CMD);// 这里要更具具体的情况 做更改
//		List<Integer> list_login_required_cmds = new ArrayList<Integer>();
//		for (Integer integer : GloableParams.login_required_cmds) {
//			list_login_required_cmds.add(integer);
//		}
		if(AppUtil.isEmpty(login_required_cmds))
			login_required_cmds = MyApp.res.getString(R.string.login_required_cmds);
		if ((","+login_required_cmds+",").contains(","+CMD+",")) {// 登陆协议和需要验证是否登陆的协议必须包含字段USR，需要验证登陆的协议只包含字段CMD、USR、SND、VER和MSG。
//			if (AppUtil.isEmpty(USR)) {
//				throw new RuntimeException("USR is null, cmd = "+cmd);
////				Toast.makeText(MyApp.context, "对不起,您还未登录", 0);
//			}
			Protocol.getInstance().setDES("1");
		}else {
			Protocol.getInstance().setDES("0");
		}

		AppUtil.put(protocol, "CMD", CMD);
		AppUtil.put(protocol, "VER", VER);
		AppUtil.put(protocol, "DES", DES);
		AppUtil.put(protocol, "USR", USR);
		AppUtil.put(protocol, "SND", SND);// 如果 snd 不为空 则 封装到 请求参数中.
		
		AppUtil.put(protocol, "A", A);
		AppUtil.put(protocol, "B", B);
		AppUtil.put(protocol, "C", C);
		AppUtil.put(protocol, "D", D);
		AppUtil.put(protocol, "E", E);
		AppUtil.put(protocol, "F", F);
		AppUtil.put(protocol, "G", G);
		AppUtil.put(protocol, "H", H);
		return protocol;
	}

	/*//这个方法其实可以放入到 Apputil 中, 便于 Message 封装时使用
	*//**
	 * 把 value 进行 url编码以后放入到 jsonobject 中,如果 value 为无效字段,比如"","  ",null, 则不操作
	 * @param jsonObject
	 * @param key
	 * @param value
	 *//*
	public static void put(JSONObject jsonObject, String key, String value) {
		if (!AppUtil.isEmpty(value))
			try {
				jsonObject.put(key, URLEncoder.encode(value, ConstantValue.CHARSET));
			} catch (Exception e) {
				Log.e("Protocal", "error in protocal");
				e.printStackTrace();
			}
	}*/

	/**
	 * 获取 协议封装好的 hashmap,如果协议的 CMD == null || VER == null || MSG == null
	 * 将会抛出运行时异常
	 * 
	 * @return
	 */
	@Deprecated
	public Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		if (CMD == null || VER == null || MSG == null) {// 任何协议中都必须包含字段CMD、VER和MSG
			throw new RuntimeException("CMD or VER or MSG is null");
		}
		int cmd = Integer.parseInt(CMD);
		if (1000 <= cmd && cmd < 1150) {// 登陆协议和需要验证是否登陆的协议必须包含字段USR，需要验证登陆的协议只包含字段CMD、USR、SND、VER和MSG。
			if (USR == null) {
				throw new RuntimeException("USR is null");
			}
			params.put("CMD", CMD);
			params.put("VER", VER);
			params.put("MSG", MSG);
			params.put("USR", USR);
			if (!AppUtil.isEmpty(SND)) {// 如果 snd 不为空 则 封装到 请求参数中.
				params.put("SND", SND);
			}
			return params;
		}
		put(params, "A", A);
		put(params, "B", B);
		put(params, "C", C);
		put(params, "D", D);
		put(params, "E", E);
		put(params, "F", F);
		return params;
	}

	@Deprecated
	private void put(Map<String, String> params, String key, String value) {
		if (!AppUtil.isEmpty(value))
			params.put(key, value);
	}

	public String getCMD() {
		return CMD;
	}

	public void setCMD(String cMD) {
		CMD = cMD;
	}

	public String getVER() {
		return VER;
	}

	public void setVER(String vER) {
		VER = vER;
	}

	public String getMSG() {
		return MSG;
	}

	public void setMSG(String mSG) {
		MSG = mSG;
	}

	public String getUSR() {
		return USR;
	}

	public void setUSR(String uSR) {
		USR = uSR;
	}

	public String getSND() {
		return SND;
	}

	public void setSND(String sND) {
		SND = sND;
	}

	public String getA() {
		return A;
	}

	public void setA(String a) {
		A = a;
	}

	public String getB() {
		return B;
	}

	public void setB(String b) {
		B = b;
	}

	public String getC() {
		return C;
	}

	public void setC(String c) {
		C = c;
	}

	public String getD() {
		return D;
	}

	public void setD(String d) {
		D = d;
	}

	public String getE() {
		return E;
	}

	public void setE(String e) {
		E = e;
	}

	public String getF() {
		return F;
	}

	public void setF(String f) {
		F = f;
	}

	public String getDES() {
		return DES;
	}

	public void setDES(String dES) {
		DES = dES;
	}

	public String getPSW() {
		return PSW;
	}

	public void setPSW(String pSW) {
		PSW = pSW;
	}

	public String getH() {
		return H;
	}

	public void setH(String h) {
		H = h;
	}

	public String getG() {
		return G;
	}

	public void setG(String g) {
		G = g;
	}
	
}
