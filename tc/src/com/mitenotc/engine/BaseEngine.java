package com.mitenotc.engine;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.app.encrypt.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.bean.UserBean;
import com.mitenotc.dao.BaseCache;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.EncryptUtil;
import com.mitenotc.net.HttpClientUtil;
import com.mitenotc.net.MessageJson;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.BuildConfig;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.SPUtil;

public class BaseEngine {

	public static MessageJson getCache(int cmd, MessageJson msg, int isforce) {
		try {
			if(msg == null){//安全性考虑, 同时有的接口不需要传入参数, msg 为空时 重新创建一个, 防止空指针异常
				msg = new MessageJson();
			}
			String classname = "com.mitenotc.dao.Cache_" + String.valueOf(cmd);
			BaseCache basehandle = (BaseCache) Class.forName(classname).newInstance();
			return basehandle.getCache(cmd, msg, isforce);
		} catch (Exception e) 
		{
			//e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @param cmd 请求命令号
	 * @param inmsg 请求参数
	 * @param outmsg 请求返回参数
	 */
	public static void setCache(int cmd, MessageJson inmsg, MessageJson outmsg) {
		String classname = "com.mitenotc.dao.Cache_" + String.valueOf(cmd);
		try {
			BaseCache basehandle = (BaseCache) Class.forName(classname)
					.newInstance();
			basehandle.setCache(cmd, inmsg, outmsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static MessageBean getCMD(int cmd, MessageJson msg)
			throws MessageException {
		int isforce = 0;
		try 
		{
			isforce = msg.isNull("isforce") ==true ? 0 : msg.getInt("isforce");
			if(!msg.isNull("isforce"))
					msg.remove("isforce");
		}
		catch (JSONException e1) 
		{
			isforce = 1;
		}
		MessageJson message = null;
		int iscache = 0;
		System.out.println("request -- cmd :"+cmd+"  msg:"+msg);
		if(isforce != 1)
				message = BaseEngine.getCache(cmd, msg, 0);
		
		if (message == null) {
			String result = "";
			result = BaseEngine.getCMD_Net(cmd, msg);
//			System.out.println("result ============== "+result);
			if (result != null) {
				try {
					JSONObject jsonResult = new JSONObject(result);// 返回的数据存在
																	// JsonException
					// 如果 返回的数据的 cmd 值不一样,则直接返回空
					JSONObject headJson = jsonResult.getJSONObject("HEADER");
					if (cmd != headJson.getInt("CMD")) {
						return null;
					}
					
					String string = jsonResult.getString("MSG");// 该 数据需要 增加 解密
					
					//解密处理
					if("1".equals(headJson.getString("DES"))){//需要解密
						string = EncryptUtil.decode(string);
					}
					
					message = new MessageJson(string);
					System.out.println(cmd+" | BaseEngine.getCMD_Net"+message.toString());
				} catch (JSONException e) {
					e.printStackTrace();
//					throw new MessageException("系统繁忙,请稍后再试");
				}
			}
		}else
		{
//			System.out.println("BaseEngine.getCache"+message.toString());
			iscache = 1;
		}
		if (message == null) {
			message = BaseEngine.getCache(cmd, msg, 1);
			if (message == null){
				if(AccountUtils.netConnectivityManager(MyApp.context)){
					throw new MessageException();
				}else{
					throw new MessageException("网络连接失败，请稍后重试!");
				}
			}else
				{
					iscache = 1;
//					System.out.println("BaseEngine.getCache"+message.toString());
				}
		}
		String resultA = message.getString("A");
//		if ("0".equals(resultA) || "10045".equals(resultA) || "11004".equals(resultA)
//			 ||"14004".equals(resultA)|| "10003".equals(resultA) || "15001".equals(resultA))
		if(Arrays.asList(MyApp.res.getStringArray(R.array.result_code_a)).contains(resultA)){// 对返回数据的处理//10045表示未登录//11004 为语音支付时要求第二次信息//10003 注册时需要前台提处理
			MessageBean gson = null;
			try {
				gson = new Gson().fromJson(message.toString(),MessageBean.class);
				// gson.setVER(jsonResult.getString("VER"));
				// 如果是登入成功,则把 用户的 session 存起来
//				System.out.println("125---base --->gson "+gson.toString());
//				if(!StringUtils.isBlank(gson.getG3())){//竞彩 G3字段需要解密
//					String g3=new String(Base64.decode(gson.getG3()));
//					System.out.println("310------------->"+g3);
//					gson.setG3(g3);
//				}
				
				if("10045".equals(resultA)){
					UserBean.getInstance().setLogin(false);
				}
				if ((cmd == 1002 || cmd == 1000)&& "0".equals(resultA)){
					Protocol.getInstance().setSND(gson.getD());
					UserBean.getInstance().setLogin(true);//用户登录成功
				}
				if(iscache != 1)
						BaseEngine.setCache(cmd, msg, message);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
			return gson;
		} else {
			if (message.isNull("B")) {// 判断错误信息是否为空
				throw new MessageException("错误信息不存在");// 这个不是给用户看的,服务端必须包含该字段
			} else
				throw new MessageException(message.getString("B"));
		}
	}

	public static String getCMD_Net(int cmd, MessageJson msg)
			throws MessageException {
		if (msg == null) {
			msg = new MessageJson();
		}
		// CMD 的设置
		Protocol.getInstance().setCMD(cmd + "");
		// 手机号的设置,其实这句代码并不是与每个 请求都关联的,不需要每个请求都做
		// Protocol.getInstance().setUSR(SPUtil.getString(R.string.USR));
		// 如果 USR值无效,则会抛出异常,需要ui层在界面中把数据存到GlobalParams中
		// 新协议的封装,把原来的协议封装到了 HEADER里, Message
		MessageJson newProtocol = new MessageJson();
		newProtocol.put("HEADER", Protocol.getInstance().getProtocol());
//		newProtocol.put("MSG", msg);
		System.out.println("protocol head ============ "+Protocol.getInstance().getProtocol());
//        System.out.println("protocol msg =========== "+msg.toString());
		//加密处理
		if(AppUtil.isEmpty(Protocol.login_required_cmds))
			Protocol.login_required_cmds = MyApp.res.getString(R.string.login_required_cmds);
		if ((","+Protocol.login_required_cmds+",").contains(","+cmd+",")) {// 需要登录的cmd 需要加密
			String encode = EncryptUtil.encode(msg.toString());
			newProtocol.put("MSG", encode);
		}else{
			newProtocol.put("MSG", msg);
		}
//		System.out.println("newProtocol ============ "+ newProtocol);
		// 获取网络层 工具类对象, 发送协议,获得返回的数据
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		String url = ConstantValue.URI;//正式环境url
//		if(BuildConfig.DEBUG){
//			url = ConstantValue.URI_test;//测试环境url
//		}
		String result = httpClientUtil.sendProtocol(url,newProtocol.toString());
		if(AppUtil.isEmpty(result)){//如果上面返回为空,则极有可能是dns 解析错误. 使用 Apputil 中的方法重新解析一次,重新请求
			result = httpClientUtil.sendProtocol(AppUtil.getHost(),newProtocol.toString());
		}
		return result;
	}
}
