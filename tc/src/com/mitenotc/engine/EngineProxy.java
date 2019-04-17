package com.mitenotc.engine;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URLDecoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mitenotc.bean.MessageBean;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.HttpClientUtil;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.GloableParams;
import com.mitenotc.utils.AppUtil;

/**
 * 业务解析的代理类,该类把业务解析的类中的方法的相同部分进行了封装.
 * 还没有添加网络的异常处理,必须添加网络异常的的try catch
 * 该类已被 BaseEngine 和 MessageJson 代替
 * @author mitenotc
 *
 */
@Deprecated
public class EngineProxy {

	public static EngineInterface proxy  = getProxy();
	
	/**
	 * @return
	 */
	public static EngineInterface getProxy(){
		EngineInterface proxy =  (EngineInterface) Proxy.newProxyInstance(EngineInterface.class.getClassLoader(), new Class[]{EngineInterface.class}, new InvocationHandler() {
			/*
			 * 还没有处理的问题:
			 * 1手机号的设置? 用GlobalValues 来传
			 * 
			 *  2直接用接口 引用接口的 proxy 是否行的通?  // 行得通
			 */
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				
				String[] split = method.getName().split("_");
				int cmd = Integer.parseInt(split[split.length-1]);
				
				// CMD 的设置
				Protocol.getInstance().setCMD(cmd+"");
				//MSG的设置
				JSONObject json = AppUtil.getMessage(args);
//				Protocol.getInstance().setMSG(json.toString());
				//手机号的设置,其实这句代码并不是与每个 请求都关联的,不需要每个请求都做
				Protocol.getInstance().setUSR(GloableParams.USR);//如果 USR 值无效,则会抛出异常,需要ui层在界面中把数据存到GlobalParams 中
				
				Protocol.getInstance().setVER("100");
				//新协议的封装,把原来的协议封装到了 HEADER里, Message
				JSONObject newProtocol = new JSONObject();
				newProtocol.put("HEADER", Protocol.getInstance().getProtocol());
				newProtocol.put("MSG", json);
				//获取网络层 工具类对象, 发送协议,获得返回的数据
				HttpClientUtil httpClientUtil = new HttpClientUtil();
				////System.out.println("protocol = "+newProtocol.toString());
				String result = httpClientUtil.sendProtocol(ConstantValue.URI, newProtocol.toString());
				JSONObject jsonResult = new JSONObject(result);
				////System.out.println("jsonresult = "+jsonResult);//测试打印代码
				//如果 返回的数据的 cmd 值不一样,则直接返回空
				if(cmd != jsonResult.getJSONObject("HEADER").getInt("CMD")){
					return null;
				}
				
				String string = jsonResult.getString("MSG");//该 数据需要 增加 解密
				JSONObject message = new JSONObject(string);
				
				//得到 url 解码 Message
//不再进行url编码,因此,也不需要解码了				JSONObject message = parseMessage(jsonResult);          ////System.out.println("proxy message = "+message);//测试打印代码
				String resultA = message.getString("A");
				if("0".equals(resultA)){//对返回数据的处理
					MessageBean gson = new Gson().fromJson(message.toString(), MessageBean.class);
//					gson.setVER(jsonResult.getString("VER"));
					//如果是登入成功,则把 用户的 session 存起来
					if(cmd == 1002)
						Protocol.getInstance().setSND(gson.getD());
					return gson;
				}else {
//					if(message.isNull("B")){
//						throw new MessageException("服务器异常");
//					}else
					throw new MessageException(message.getString("B"));
				}
			}
		});
		return proxy;
	}
	/**
	 * 对数据的每个 标签和内容进行 url 解码,主要是 message 部分
	 * @param jsonResult
	 * @return
	 * @throws JSONException
	 */
	private static JSONObject parseMessage(JSONObject jsonResult)
			throws JSONException {
		String decode = URLDecoder.decode(jsonResult.getString("MSG"));
		JSONObject jsonObject = new JSONObject(decode);
		JSONObject urlDecodeJson = AppUtil.urlDecodeJson(jsonObject);
		return urlDecodeJson;
	}
}
