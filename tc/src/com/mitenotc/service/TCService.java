package com.mitenotc.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.dao.Cache_1353;
import com.mitenotc.dao.Cache_1358;
import com.mitenotc.dao.ImageFileCache;
import com.mitenotc.engine.BaseEngine;
import com.mitenotc.exception.MessageException;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.R;
import com.mitenotc.tc.TCTimerHelper;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.FormatUtil;
import com.mitenotc.utils.SPUtil;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract.Constants;
import android.widget.TabHost;

public class TCService extends Service{
	
	/** 轮询 */
	private long preTextChangeTime = 0;
	public static long TIME_INTERVAL=60;// 默认60秒轮询一次
	private Intent bcIntent;
	private Thread t;
	ImageFileCache filecache = new ImageFileCache();
	public static String CMD_STOP_SERVICE="android.intent.action.Stop_TCService";//停止广播action,关闭TCService
	public static String CMD_START_SERVICE="android.intent.action.Start_TCService";//开启广播action, 打开TCService
	private GovernServiceReceiver mReceiver;
	@Override
	public IBinder onBind(Intent intent) {//调用bindService的时候调用,只有当调用了bindService的时候才能通过Binder 中的方法类调用Service中的方法
		return null;
	}
	@Override
	public void onCreate() {//只会在该服务被创建的时候调用
		super.onCreate();
		bcIntent=new Intent();
		t = new Thread(){
			@Override
			public void run() 
			{
			 super.run();
			 MessageJson msg = new MessageJson();
			 msg.put("B", "10000");
			 msg.put("C", "2");
			 
			 while(true)
			 {
			try {
					 if(System.currentTimeMillis()-preTextChangeTime > TIME_INTERVAL*1000 
							 && FormatUtil.formatSheduleTimer(new SimpleDateFormat("HH:mm:ss").format(new Date())))//&& FormatUtil.formatSheduleTimer(new SimpleDateFormat("HH:mm:ss").format(new Date()))
					 {
						 System.out.println("1358---TIME_INTERVAL--->"+TIME_INTERVAL*1000);
						MessageBean msgbean = BaseEngine.getCMD(1358, msg);
						preTextChangeTime = System.currentTimeMillis();// 一次请求结束时间
						if(msgbean.getLIST()!=null && msgbean.getLIST().size() >0)
						{
							bcIntent.putExtra("MSG_COUNT", Cache_1358.getMSG_COUNT());
							SPUtil.putInt(R.string.msg_count, Cache_1358.getMSG_COUNT());//存储客服留言条目
							bcIntent.setAction("com.mitenotc.ui.account.ConsultationFragment");
							sendBroadcast(bcIntent);
						}
					 }else{
						 if(MyApp.loadimageList.size()==0){
							 synchronized (this) {
//								 Thread.sleep(TIME_INTERVAL*1000);//2014-11-25放弃
								this.wait(TIME_INTERVAL*1000);
							 }
						 }
				     }
//					 System.out.println("filename s"+MyApp.loadimageList.size());	
					 if(MyApp.loadimageList.size()>0)
					 {
						 for(int i=0;i<MyApp.loadimageList.size();i++)
						 {
//						System.out.println("filename"+MyApp.loadimageList.get(i));	 
						 if(filecache.downloadImage(MyApp.loadimageList.get(i)))
							 MyApp.loadimageList.remove(i);
//						 System.out.println("filename for"+MyApp.loadimageList.get(i));	 
						 
						 }
					 }
				} catch (Exception e) {
						e.printStackTrace();
				}
			}
			}
		};
		t.start();
	}
	
	
  
	@Override
	public void onStart(Intent intent, int startId) {//每次调用startService 方法的时候调用
		super.onStart(intent, startId);
		if(t!=null){//intent.getAction().equals("com.mitenotc.intent.TCService.start")
	        synchronized (this.t) {
				 t.notify();
			 }
	     }
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mReceiver=new GovernServiceReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.Start_TCService");
		filter.setPriority(1000);//优先级
		registerReceiver(mReceiver, filter);
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {//调用unBindServicde的时候调用
		return super.onUnbind(intent);
	}
	public static class GovernServiceReceiver  extends BroadcastReceiver{
		@Override
		public void onReceive(Context ctx, Intent data) {
//			System.out.println("1358-----onReceive------>"+data.toString());
			if(!StringUtils.isBlank(data.getAction())){
				if(CMD_STOP_SERVICE.equals(data.getAction())){
					TIME_INTERVAL= 60 * 5;
//					stopSelf();//停止服务
//					System.out.println("1358-t---------->"+TIME_INTERVAL);
				}else if(CMD_START_SERVICE.equals(data.getAction())){
					TIME_INTERVAL= 60;
//					Intent tc_service=new Intent(MyApp.context,TCService.class);
//					tc_service.setAction(MyApp.TCCONSULT_SERVICE);
//                   MyApp.context.startService(tc_service);
//					System.out.println("1358-t---------->"+TIME_INTERVAL);
				}
			}
		}
	}
}
