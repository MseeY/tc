package com.mitenotc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;

import com.mitenotc.tc.ConstantValue;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;

public class AppUtil {

	/**
	 * 获取当前页面的屏幕高度
	 * @param cx
	 * @return
	 */
	public static int getDisplayHeight(Context cx) {
		DisplayMetrics dm =  cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;
		return screenHeight;
	}

	/**
	 * 获取当前页面的屏幕宽度
	 * 
	 * @param cx
	 * @return
	 */
	public static int getDisplayWidth(Context cx) {
		DisplayMetrics dm =  cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;

		return screenWidth;
	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	public static String getVersion(Context cx) {
		PackageManager pm = cx.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(cx.getPackageName(),0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 不可能发生
			// can't reach
			return "";
		}
	}
	
	public static Bundle getMetaData(Context cx){
		PackageManager pm = cx.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(cx.getPackageName(),PackageManager.GET_META_DATA);
			return packageInfo.applicationInfo.metaData;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// 不可能发生
			// can't reach
			return null;
		}
	}
	
	/**
	 * 获取 IEMI 信息
	 * @param cx
	 * @return
	 */
	public static String getIEMI(Context cx){
		TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);
		 String deviceId = tm.getDeviceId();
		 if(deviceId == null)
			 return "";
		return deviceId;
	}
	
	/**
	 * 获取 IMSI 信息
	 * @param cx
	 * @return
	 */
	public static String getIMSI(Context cx){
		TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);
		String subscriberId = tm.getSubscriberId();
		if(subscriberId == null)
			return "";
		return subscriberId;
	}
	
	/**
	 * 获取 sim 卡 序列号
	 * @param cx
	 * @return
	 */
	public static String getSimSerialNumber(Context cx){
		TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}
	/**
	 * 获取手机号码
	 * @param cx
	 * @return
	 */
	public static String getLine1Number(Context cx){
		TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
	
	/**
	 * 判断字符串是否有效: 主要有 "", "     ", null 这三种
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if(str==null)
			return true;
		return TextUtils.isEmpty(str.trim());
	}
	
	/**
	 * json 的put 方法,把 value 放入到 jsonobject 中,如果 value 为无效字段,比如"","  ",null, 则不操作
	 * @param jsonObject
	 * @param key
	 * @param value
	 */
	public static void put(JSONObject jsonObject, String key, String value) {
		if (!AppUtil.isEmpty(value))
			try {
				jsonObject.put(key, value);
//				jsonObject.put(key, URLEncoder.encode(value, ConstantValue.CHARSET));
			} catch (Exception e) {
				Log.e("Protocal", "error in protocal");
				e.printStackTrace();
			}
	}
	/**
	 * 主要是对 Message 中的每一条 内容进行 url解码
	 * @param jsonObject
	 * @return
	 */
	public static JSONObject urlDecodeJson(JSONObject jsonObject){//注意要对其中的 List 也进行处理,等下添加回去
		Iterator keys = jsonObject.keys();
		JSONObject json = new JSONObject();
		while(keys.hasNext()){
			String key = (String) keys.next();
			try {
				json.put(key, URLDecoder.decode(jsonObject.getString(key),"gbk"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	/**
	 * 对协议中的 消息进行封装,如果 args 中有List,则list 中的对象 必须为 map 型
	 * 该方法已废弃,改为使用 BaseEngine 和 MessageJson
	 * 在对代码进行修改后,考虑到程序的扩展性,把getMessage的封装解除了,所有的message,现在有界面层创建一个MessageJson 如何封装到该json中直接传入到 BaseEngine的 getCMD 中
	 * @param args
	 * @return
	 */
	@Deprecated 
	public static JSONObject getMessage(Object[] args){
		JSONObject json = new JSONObject();
		if(args == null)
			return json;
		
		try {
			for (int i = 0; i < args.length; i++) {
				if(args[i] instanceof List){
					
					List list = (List) args[i];
					if(list.size() == 0)
						continue;
					
					JSONArray jsonArray = new JSONArray();
					for (Object object : list) {
						Map<String, String> map = (Map<String, String>) object;
						JSONObject map2json = map2Json(map);
						jsonArray.put(map2json);
					}
					json.put("LIST", jsonArray);
					continue;
				}
				
				Character c =  (char) ('A' + i);
				put(json,c.toString(), (String) args[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 把map中的每个 key 和 value 都拿出来,然后存入 json中,并对 其中的key 进行 url 编码
	 * @param map
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException 
	 */
	private static JSONObject map2Json(Map<String, String> map)
			throws JSONException, UnsupportedEncodingException {
		JSONObject map2json = new JSONObject();
		Set<Map.Entry<String, String>> entrySet = map.entrySet();
		for (Map.Entry<String, String> entry : entrySet) {
			put(map2json,entry.getKey(), entry.getValue());
		}
		return map2json;
	}
	/**
	 * 对协议中的 消息进行封装
	 * 对该方法修改后,使用 getMessage()
	 * @param args
	 * @return
	 */
	@Deprecated
	public static JSONObject getMessage1(Object[] args){
		JSONObject json = new JSONObject();
		if(args == null)
			return json;
		try {
			for (int i = 0; i < args.length; i++) {
				if(args[i] instanceof List){
					List list = (List) args[i];
					Object[] array = list.toArray();
					JSONObject messageList = getMessage(array);
					json.put("list", messageList);
					continue;
				}
				
				Character c =  (char) ('A' + i);
				json.put(c.toString(), args[i]);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static <T>void copyList(List<T> res, List<T> des){
		for (T t : res) {
			des.add(t);
		}
	}
	
	public static void testTime(int i){
		////System.out.println("apptime"+i+" = "+(System.currentTimeMillis() - MyApp.appTestTime));
	}
	
	public static boolean isRunningForeground(Context context){
        String packageName=MyApp.context.getPackageName();
        String topActivityClassName=getTopActivityName();
        ////System.out.println("packageName="+packageName+",topActivityClassName="+topActivityClassName);
        if (packageName!=null&&topActivityClassName!=null&&topActivityClassName.startsWith(packageName)) {
            ////System.out.println("---> isRunningForeGround");
            return true;
        } else {
            ////System.out.println("---> isRunningBackGround");
            return false;
        }
    }
     
     
    public  static String getTopActivityName(){
        String topActivityClassName=null;
         ActivityManager activityManager =
        (ActivityManager)(MyApp.context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
         List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1) ;
         if(runningTaskInfos != null){
             ComponentName f=runningTaskInfos.get(0).topActivity;
             topActivityClassName=f.getClassName();
         }
         return topActivityClassName;
    }
    
    public static boolean isNumeric(String str){
    	if(AppUtil.isEmpty(str))
    		return false;
    	try {
			long parseLong = Long.parseLong(str.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
    }
    public static boolean isDouble(String str){
    	if(AppUtil.isEmpty(str))
    		return false;
    	try {
    		double parseLong = Double.parseDouble(str.trim());
    		return true;
    	} catch (NumberFormatException e) {
    		return false;
    	}
    }

    
    public static int parseInt(String num){
    	if(AppUtil.isNumeric(num)){
    		return Integer.parseInt(num);
    	}
    	return 0;
    }
    
    /**
     * 在dns 解析地址失败的情况下 可以调用该方法来获取ip地址
     * @return
     */
    public static String getHost(){
    	String ip = "";
    	try {
			InetAddress inet = InetAddress.getByName(ConstantValue.URI);
			ip = inet.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return ip;
    }
    
	public static void saveToFile(String str){
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"save.txt"), true));
			pw.print(str + "\n");
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
