package com.mitenotc.tc;

import com.mitenotc.ui.base.BaseFragment;

import android.content.Context;

/**
 * 全局变量
 * 
 * @author Administrator
 * 
 */
public class GloableParams {
	public static String USR = "13866667777";
	public static String SND = "";
	/**
	 * 代理的ip 只对wap方式有效
	 */
	public static String PROXY_IP = "";
	/**
	 * 代理的端口 只对wap方式有效
	 */
	public static int PROXY_PORT = 0;
	
	public static Context context;
	
	public static boolean isLoading = false;

//	public static int[] login_required_cmds = new int[]{};
//	public static Class bottom_check = null;
}
