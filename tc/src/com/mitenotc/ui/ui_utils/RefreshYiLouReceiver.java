package com.mitenotc.ui.ui_utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mitenotc.tc.MyApp;
import com.mitenotc.ui.play.RefreshYiLou;

public class RefreshYiLouReceiver extends BroadcastReceiver {
      private static RefreshYiLou mRefreshYiLou;
    
      public  String ACTION_START="";
	  private String ACTION_STOP="";
	  private String ACTION_AWARDINFO="";
	  // 为空控制广播次数
	  private int ACTION_STOP_INT = 0;
	  
	@Override
	public void onReceive(Context context, Intent intent) {////System.out.println("onreceived action = "+intent.getAction());
            String action=intent.getAction();
            if(ACTION_START.equals(action) ){
            	if(mRefreshYiLou!=null){
            		mRefreshYiLou.onReceiveYilou_start();
            	}
            }else if(ACTION_STOP.equals(action) && 0 == ACTION_STOP_INT){
            	if(mRefreshYiLou!=null){
            		ACTION_STOP_INT += 1;
            		mRefreshYiLou.onReceiveYilou_stop();
            	}
            }else if(ACTION_AWARDINFO.equals(action) ){
            	if(mRefreshYiLou!=null){
            		mRefreshYiLou.onReceiveYilou_awardinfo();
            	}
            }
            	
	}
	
	public RefreshYiLouReceiver(String lottery_id) {
		super();
		//为了控制次数 定义的数量
		ACTION_STOP_INT = 0;
		ACTION_START="com.mitenotc.ui.play.on_lottery_"+lottery_id+"_start_loading";
		ACTION_STOP="com.mitenotc.ui.play.on_lottery_"+lottery_id+"_stop_loading";
		ACTION_AWARDINFO="com.mitenotc.ui.play.on_lottery_"+lottery_id+"_awardinfo_received";
	}

	public static RefreshYiLou getmRefreshYiLou() {
		return mRefreshYiLou;
	}

	public  void setmRefreshYiLou(RefreshYiLou mRefreshYiLou) {
		RefreshYiLouReceiver.mRefreshYiLou = mRefreshYiLou;
	}


	

}
