package com.mitenotc.ui.ui_utils;



import com.mitenotc.tc.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

/**
 * 
 * 朱万利
 * 
 * 提示信息的管理
 */

public class PromptManager {
	// 滚动条
	private static ProgressDialog dialog;

	// 提示框
	// Toast

	public static void showProgressDialog(Context context) {
		dialog = new ProgressDialog(context);
		dialog.setTitle(R.string.app_name);
        dialog.setMessage("请等候，注册信息提交中……");
		dialog.show();

	}
	// 提示框-关闭
	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	public static void showNoNetWork(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.app_name)//
				.setMessage("当前无网络").setPositiveButton("设置", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyToast.showToast(context, "跳到诊断网络环境页面！   待实现");
//						// 跳转到系统的网络设置界面
//						Intent intent=new Intent();
//						intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
//						context.startActivity(intent);
						
					}
				}).setNegativeButton("知道了", null).show();
	}
	
	// 退出系统
	public static void showExitSystem(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle(R.string.app_name)//
				.setMessage("是否退出应用").setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				})//
				.setNegativeButton("取消", null)//
				.show();

	}
	
	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showToast(Context context, int msgResId) {
		Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show();
	}
	//当测试阶段时true
	private static final boolean isShow = true;

	/**
	 * 测试用
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastTest(Context context, String msg) {
		if (isShow) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new AlertDialog.Builder(context)//
				.setTitle(R.string.app_name)//错误信息标题
				.setMessage(msg)//
				.setNegativeButton(context.getString(R.string.is_positive), null)//关闭
				.show();
	}

}
