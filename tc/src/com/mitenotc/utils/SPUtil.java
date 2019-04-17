package com.mitenotc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mitenotc.tc.MyApp;

/**
 * sharedpreference 工具类
 * @author mitenotc
 *
 */
public class SPUtil {

	public static SharedPreferences sp = MyApp.context.getSharedPreferences("TCSP", Context.MODE_PRIVATE);
	
	public static boolean delSP(){
		Editor edit = sp.edit();
		return edit.clear().commit();
	}
	/**
	 * 获取 string类型,默认值为 "";
	 * @param key
	 * @return
	 */
	public static String getString(String key){
		return sp.getString(key, "");
	}
	/**
	 * 直接用 string资源文件 中的id
	 * @param resId
	 * @return
	 */
	public static String getString(int resId){
		return getString(MyApp.res.getString(resId));
	}
	
	/**
	 * 获取 String类型,并自定义默认值
	 * @param key
	 * @param defVal
	 * @return
	 */
	public static String getString(String key,String defVal){
		return sp.getString(key,defVal);
	}
	/**
	 * 往 sharedPreference 中添加String类型的数据
	 * @param key
	 * @param value
	 */
	public static void putString(String key,String value){
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
		edit = null;
	}
	/**
	 * 往 sharedPreference 中添加int类型的数据
	 * @param key
	 * @param value
	 */
	public static void putString(String key,int value){
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
		edit = null;
	}

	/**
	 * 调用资源文件中统一的 id, 来统一使用 SharedPreference
	 * @param keyId
	 * @param value
	 */
	public static void putString(int keyId,String value){
		String key = MyApp.res.getString(keyId);
		putString(key, value);
	}
	/**
	 * 调用资源文件中统一的 id, 来统一使用 SharedPreference
	 * @param keyId
	 * @param value
	 */
	public static void putString(int keyId,int value){
		String key = MyApp.res.getString(keyId);
		putString(key, value);
	}
	
	/**
	 * 获取 Boolean类型,并自定义默认值
	 * @param key
	 * @param defVal
	 * @return
	 */
	public static boolean getString(String key,Boolean value){
		return sp.getBoolean(key,value);
	}
	/**
	 * 往 sharedPreference 中添加boolean类型的数据
	 * @param key
	 * @param value
	 */
	public static void putBoolean(String key,Boolean value){
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
		edit = null;
	}

	/**
	 * 获取boolean的数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}
	
	/**
	 * 往 sharedPreference 中添加int类型的数据
	 * @param key
	 * @param value
	 */
	public static void putInt(String key,int cmd){
		Editor edit = sp.edit();
		edit.putInt(key, cmd);
		edit.commit();
		edit = null;
	}
	/**
	 * 往 sharedPreference 中添加int类型的数据
	 * @param key
	 * @param value
	 */
	public static void putInt(int keyId,int value){
		Editor edit = sp.edit();
		String key = MyApp.res.getString(keyId);
		edit.putInt(key, value);
		edit.commit();
		edit = null;
	}

	/**
	 * 获取int的数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(String key,int cmd){
		return sp.getInt(key, cmd);
	}
	/**
	 * 获取int的数据
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(int keyId,int cmd){
		String key = MyApp.res.getString(keyId);
		return sp.getInt(key, cmd);
	}
}
