package com.mitenotc.ui.ui_utils;
import android.content.Context;
import android.widget.Toast;
import android.content.Context;
import android.widget.Toast;

/**
 * 自定义Toast
 * 
 * 朱万利
 * @date 日期: 2013-12-15
 */
public class MyToast {
	
    private static Toast toast;
	private static  boolean  TEST=true;
	
	/**
	 * 测试使用
	 * @param context 上下文
	 * @param msg String 类型的msg
	 */
	
	public static void showTestToast(Context context,String msg){
		
		if(toast == null&& TEST==true){
			toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		}else{
			toast.setText(msg);
		}
			toast.show();
	}
	
	/**
	 * 应用中的需要   Toast.LENGTH_SHORT
	 * @param context 上下文 
	 * @param resId  String  resours id
	 */
	public static void showToast(Context context,int resId){
		
		if(toast == null){
			toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
		}else{
			toast.setText(resId);
		}
		toast.show();
	}
	/**
	 * 应用中的需要   Toast.LENGTH_SHORT
	 * @param context 上下文 
	 * @param resId  String  resours id
	 */
	public static void showToast(Context context,String str){
		
		if(toast == null){
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		}else{
			toast.setText(str);
		}
		toast.show();
	}
	
	/**
	 * 应用中的需要  Toast.LENGTH_LONG
	 * @param context 上下文 
	 * @param resId  String  resours id
	 */
	public static void showToastLong(Context context,int resId){
		
		if(toast == null){
			toast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
		}else{
			toast.setText(resId);
		}
		toast.show();
	}
	/**
	 * 应用中的需要  Toast.LENGTH_LONG
	 * @param context 上下文 
	 * @param resId  String  resours id
	 */
	public static void showToastLong(Context context,String str){
		
		if(toast == null){
			toast = Toast.makeText(context, str, Toast.LENGTH_LONG);
		}else{
			toast.setText(str);
		}
		toast.show();
	}
}

