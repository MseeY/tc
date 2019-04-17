package com.mitenotc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.mitenotc.dao.CacheDBHelper;
import com.mitenotc.net.Protocol;
import com.mitenotc.tc.MyApp;
import com.mitenotc.tc.TCActivity;
import com.mitenotc.utils.JPTagAliasCallback;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
//		Toast.makeText(context, "on boot receiver miteno", 0).show();
//		Intent intent2 = new Intent(context,TCActivity.class);
//		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//如果没有加这行代码,新开的activity 会报错
//		context.startActivity(intent2);
		JPushInterface.init(context);     		// 初始化 JPush
       MyApp.datacache = new CacheDBHelper(context);
        
		JPushInterface.setAliasAndTags(context.getApplicationContext(),Protocol.getInstance().getC(),null, new JPTagAliasCallback());  
	}

}
