package com.mitenotc.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.mitenotc.tc.GloableParams;

/**
 * 判断网络类型
 * 
 * @author Administrator
 * 
 */
public class NetUtil {
	/**
	 * 检查网络工具
	 * 
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		// 判断WIFI连接
//		boolean isWIFI = isWIFIConnectivity(context);
		/*// 判断是APNList连接// 当前版本 取消了 apn 连接
		boolean isMobile = isMobileConnectivity(context);

		if (!isWIFI && !isMobile) {
			// 提示用配置网络
			return false;
		}

		// 如果Mobile连接
		if (isMobile) {
			// 判断wap还是net，如果是wap，设置ip和端口
			// 读取当前处于连接的apn信息，ip和端口是否有值
			// 读取操作
			readAPN(context);
		}*/

		return true;
	}

	/**
	 * 读取操作——联系人的读取
	 * 
	 * @param context
	 */
	private static void readAPN(Context context) {
		Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");// 4.0模拟器屏蔽掉该权限

		ContentResolver contentResolver = context.getContentResolver();
		Cursor query = contentResolver.query(PREFERRED_APN_URI, null, null, null, null);
		// 获取到的内容只有一条，当前正在处于连接状态的apn记录
		if (query != null && query.moveToFirst()) {
			GloableParams.PROXY_IP = query.getString(query.getColumnIndex("proxy"));
			GloableParams.PROXY_PORT = query.getInt(query.getColumnIndex("port"));
		}

	}

	/**
	 * 判断手机接入点连接状态
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isMobileConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}

	/**
	 * 判断WIFI连接状态
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (networkInfo != null) {
			return networkInfo.isConnected();
		}

		return false;
	}
}
