package com.mitenotc.ui.account;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.mitenotc.tc.GloableParams;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.ui.ui_utils.MemoryManager;
import com.mitenotc.utils.SPUtil;

public class DownLoadUtil extends Activity{

		// 下载进度条
		private ProgressBar progressBar;
		// 进度条显示数值
		private int progress = 0;
		// SD卡路径
		private String spathSD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/taicai/";
		// 手机路径
		private String spathSJ = Environment.getRootDirectory().getAbsolutePath()+"/taicai/";
		
		private String apkPath;
		
		public Dialog dialog;
		
		public String path;
		
		private AlertDialog.Builder builder;
		private LayoutInflater inflater;
		private Context mActivity;
		
		public DownLoadUtil(Context mActivity) {
			this.mActivity = mActivity;
		}

		public void createDialog(String path){
			this.path = path;
			builder = new AlertDialog.Builder(mActivity);
			builder.setIcon(R.drawable.notify_left);//图标
			builder.setTitle("升级中,请稍后...");
			builder.setCancelable(false);
			inflater = LayoutInflater.from(mActivity);  
			View v = inflater.inflate(R.layout.update_prgress, null);
			progressBar = (ProgressBar) v.findViewById(R.id.pb_update_progress);
			builder.setView(v);
			dialog = builder.create();
			dialog.show();
			downLoadNewVerson(path);
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			if(dialog.isShowing()){
				dialog.dismiss();
			}
		}
		
		private NotificationManager notificationManager;
		private Notification notification;
		private Intent updateIntent;
		private PendingIntent pendingIntent;
		private int notification_id = 0;

		/***
		 * 创建通知栏
		 */
		private RemoteViews contentView;
		
		public void createNotification() {
			notificationManager = (NotificationManager) mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
			int icon = R.drawable.ic_launcher;;
	        CharSequence tickerText = "开始下载";
	        long when = System.currentTimeMillis();
	        notification = new Notification(icon, tickerText, when);
			notification.flags = Notification.FLAG_AUTO_CANCEL;

			contentView = new RemoteViews(mActivity.getPackageName(),R.layout.m_acc_notification_item);
			contentView.setTextViewText(R.id.notificationTitle, "正在下载");
			contentView.setTextViewText(R.id.notificationPercent, "0%");
			contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

			notification.contentView = contentView;

			updateIntent = new Intent(mActivity, SettingFragment.class);
			updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			pendingIntent = PendingIntent.getActivity(mActivity, 0, updateIntent, 0);
			notification.contentIntent = pendingIntent;
			notificationManager.notify(notification_id, notification);
		}
		
		public void downLoadNewVerson(final String path) {
			new Thread() {
				public void run() {
					//如果外置存储卡足够用，就下载到外置存储卡  暂时设置为4兆
					if (MemoryManager.externalMemoryAvailable()&& MemoryManager.getAvailableExternalMemorySize() > 5 * 1024 * 1024) {
						startDownLoad(spathSD);
					//如果该手机有root权限，就下载到手机内存
					} else if(AccountUtils.isRoot() && MemoryManager.getAvailableInternalMemorySize() > 5 * 1024 * 1024){
						startDownLoad(spathSJ);
					} else{
						Toast.makeText(mActivity, "手机存储空间已满，下载失败！", Toast.LENGTH_SHORT).show();
						return;
					}
				};
			}.start();
		}
		
		//开始下载界面
		private void startDownLoad(String spath){
			
			File file = new File(spath);
			if (!file.exists()) {
				file.mkdir();
			} else {
				DownLoadFileControl control = new DownLoadFileControl();
				control.deleteDirectory(spath);
				file.mkdir();
			}
			
			FileOutputStream fos;
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				int length = conn.getContentLength(); 
				int code = conn.getResponseCode();
				if (code == 200) {
					GloableParams.isLoading = true;
					apkPath = spath+AccountUtils.getFileName(path);
					File filePath = new File(apkPath);
					InputStream is = conn.getInputStream();
					fos = new FileOutputStream(filePath);
					int len = 0;
					int down_step = 1;// 提示step
					int updateCount = 0;// 已经上传的文件大小
					int total = 0; // 已经下载好的
					byte[] buffer = new byte[2048];
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
						total += len;
						progress = (int) (((float) total / length) * 100);
						handler.sendEmptyMessage(1);
						if (updateCount == 0|| (total * 100 / length - down_step) >= updateCount) {
							updateCount += down_step;
							// 更新进度情况
							contentView.setTextViewText(R.id.notificationPercent,updateCount + "%");
							contentView.setProgressBar(R.id.notificationProgress, 100,updateCount, false);
							// show_view
							notificationManager.notify(notification_id,notification);
						}
					}
					
					if(fos!= null){
						fos.close();
					}
					if(is!=null){
						is.close();
					}
					
					GloableParams.isLoading = false;
					contentView.setTextViewText(R.id.notificationTitle,"下载完成");
					notificationManager.notify(notification_id,notification);
					handler.sendEmptyMessage(0);
					
					
				}else{
					Toast.makeText(mActivity, "请您检查网络!", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				GloableParams.isLoading = false;
			}
		}
		
		/**
		 * 声明一个handler来跟进进度条
		 */
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					progressBar.setProgress(progress);
					break;
				case 0:
					if(dialog.isShowing()){
						dialog.dismiss();
					}
					SPUtil.putString(SPUtil.getString(MyApp.res.getString(R.string.SPLASH)), "");
					installApk();
					break;
				case 2:
					Toast.makeText(mActivity, "无内存卡！", Toast.LENGTH_SHORT).show();
					break;
				}
			};
		};

		/**
		 * 安装apk
		 */
		private void installApk() {
			
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(apkPath)),"application/vnd.android.package-archive");
			mActivity.startActivity(intent);
		}

}
