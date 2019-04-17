package com.mitenotc.tc;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.mitenotc.enums.CustomTagEnum;
import com.mitenotc.ui.TCDialogs;
import com.mitenotc.ui.TCDialogs.OkClickedListener;
import com.mitenotc.ui.base.BaseActivity;
import com.mitenotc.utils.AppUtil;
import com.mitenotc.utils.JPTagAliasCallback;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，
 * 则：
 *  1)默认用户会打开主界面 
 *  2)接收不到自定义消息
 */

public class MyReceiver extends BroadcastReceiver {

	private static final String TAG = "JPush";
	  
	private Context mContext;
	Intent newintent ;
	Bundle newbundle ; 
	
	@Override
	public void onReceive(Context context, Intent intent){
		try {
			this.mContext = context;
			
			Bundle bundle = intent.getExtras();
			if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
				Log.d(TAG,"[MyReceiver] 接收到推送下来的自定义消息: "+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
				System.out.println(";;;;;;;;;;"+bundle.getString(JPushInterface.EXTRA_MESSAGE));
				JSONObject jsonInfos = new JSONObject(bundle.getString(JPushInterface.EXTRA_MESSAGE));////System.out.println("jsonInfos ====== "+jsonInfos);
				//对消息类型 05 的处理
				if("05".equals(jsonInfos.getString("A"))){//有新的开奖号码
					context.sendBroadcast(new Intent("com.mitenotc.ui.play.on_lottery_"+jsonInfos.getString("B")+"_awardinfo_received"));//获取新开奖号码的广播
					System.out.println("jsonInfos.getString(B) ============= "+jsonInfos.getString("B"));
					return;
				}
	
				if(!"04".equals(jsonInfos.getString("A"))){
					SetNotification(jsonInfos);
				}else
				{
					Set<String> tags =  new LinkedHashSet<String>();
					tags.add(jsonInfos.getString("B"));
					JPushInterface.setAliasAndTags(mContext,null, tags, new JPTagAliasCallback());        	
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void  SetNotification(JSONObject info) {
		
		try{
		newintent = new Intent(mContext, TCActivity.class);
		newbundle = new Bundle(); 			
		String switchstr = info.getString("A");
		String leftbtnText="立即查看";// 按钮内容 为Z的内容可以为： 立即注册； 在建议用户充值时，可以提示成：马上充值  ； 而不是现在的统一   立即查看了。
		//1 开启服务
		NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		//2构建通知
		Notification notification = new Notification();
		notification.icon = R.drawable.notify_left;   // 这个图标必须要设置，不然下面那个RemoteViews不起作用.
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults =Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;   
		notification.defaults |= Notification.DEFAULT_SOUND;
		
		if("00".equals(switchstr))
		{
			notification.tickerText = Html.fromHtml(info.getJSONObject("B").getString("B"));

			newbundle.putString("url", info.getJSONObject("B").getString("E"));
			newintent.putExtra("window_id","10000");
			newintent.putExtras(newbundle);
		} else if("01".equals(switchstr))
		{
			notification.tickerText = Html.fromHtml(info.getJSONObject("B").getString("A"));		
			newbundle.putString("Notification", info.getJSONObject("B").toString());
			newintent.putExtra("window_id",info.getJSONObject("B").getString("C"));
			newintent.putExtras(newbundle);
			if(!info.getJSONObject("B").isNull("Z") ){
				leftbtnText=info.getJSONObject("B").getString("Z");
			}
//			System.out.print("bbbbbbbbbbbbbbbbbbbbb"+newintent.getStringExtra("window_id"));
			
		} else if("02".equals(switchstr))
		{
			notification.tickerText = Html.fromHtml(info.getJSONObject("B").getString("A"));
			//notification.contentView
			newbundle.putString("Notification", info.getJSONObject("B").toString());
			newintent.putExtra("window_id","0");
			newintent.putExtras(newbundle);	
			if(!info.getJSONObject("B").isNull("Z") ){
				leftbtnText=info.getJSONObject("B").getString("Z");
			}
		} else if("03".equals(switchstr))
		{
			notification.tickerText = Html.fromHtml(info.getJSONObject("B").getString("C"));
			 int msgtype = info.getJSONObject("B").getInt("A");
			if(msgtype == 3 ||  msgtype == 4)
			{
			newbundle.putString("orderId", info.getJSONObject("B").getJSONObject("B").getString("A"));
//			newbundle.putString("lotteryId", info.getJSONObject("B").getJSONObject("B").getString("B"));
//			newbundle.putString("lotteryName", info.getJSONObject("B").getJSONObject("B").getString("B1"));
//			newbundle.putString("issue", info.getJSONObject("B").getJSONObject("B").getString("C"));
//			newbundle.putString("orderAmount", info.getJSONObject("B").getJSONObject("B").getString("E"));
//			newbundle.putString("orderAward", info.getJSONObject("B").getJSONObject("B").getString("G"));
//			
//			newbundle.putString("orderState", info.getJSONObject("B").getJSONObject("B").getString("H0"));
//			newbundle.putInt("order_state", Integer.valueOf(info.getJSONObject("B").getJSONObject("B").getString("H")));
//			
//			newbundle.putString("orderTime", info.getJSONObject("B").getJSONObject("B").getString("D"));
			
			if("1".equals(info.getJSONObject("B").getJSONObject("B").getString("F")))
				newintent.putExtra("window_id","1013"); //不追号普通玩法
			else if("2".equals(info.getJSONObject("B").getJSONObject("B").getString("F")))
				newintent.putExtra("window_id","1014"); //追号普通玩法
			else
				newintent.putExtra("window_id","0");
			}else
				newintent.putExtra("window_id","1003");			
			newintent.putExtras(newbundle);
			
			if(!info.getJSONObject("B").isNull("Z") ){
				leftbtnText=info.getJSONObject("B").getString("Z");
			}
		}else
			newintent.putExtra("window_id","0");

		////System.out.println("xxxxxxxxxxxcccccccc"+newintent.getStringExtra("window_id"));
		if(AppUtil.isRunningForeground(mContext))
		{
			////System.out.println("dddddddddddddddddd"+newintent.getStringExtra("window_id"));
			if(MyApp.activityList !=null && MyApp.activityList.size()>0)
			{
				final TCDialogs dialog = new TCDialogs((BaseActivity)MyApp.activityList.get(MyApp.activityList.size()-1));
				dialog.setCancelable(false);
				if(GloableParams.isLoading == true){
					return;
				}
				
				dialog.popPushMsg(leftbtnText,notification.tickerText,new  OkClickedListener()
				{
					@Override
					public void okClicked() {
						dialog.dismiss();
							 String window_idstr = newintent.getStringExtra("window_id");
							 if(!AppUtil.isEmpty(window_idstr)){
							 int window_id = Integer.parseInt(window_idstr);						 
							 if(window_id==0)
								 return ;
							 MyApp.backToTCActivity();
							 newintent.putExtra("window_id", "0");
							 CustomTagEnum.startActivityWithId((BaseActivity)MyApp.activityList.get(0),newbundle,window_id);
							 }
							 
					}
				});
//				saveInfoJson(info);
			}else
			{
				Toast.makeText(mContext, notification.tickerText, Toast.LENGTH_LONG).show();
			}
		}
		else
		{
			//RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.push_notify); 
	        //contentView.setImageViewResource(R.id.image, R.drawable.notify_left); 
	        //contentView.setTextViewText(R.id.notify_msg_big_msg, notification.tickerText);       
	        //notification.contentView = contentView;
	        
			newintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);			
			PendingIntent contentnewintent = PendingIntent.getActivity(mContext, R.string.app_name,newintent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(mContext, notification.tickerText, null, contentnewintent);
			//notification.contentIntent = contentnewintent;
			notificationManager.notify(1, notification);
			
			saveInfoJson(info);
		}
		}catch(Exception e){
			e.printStackTrace();
			System.out.print(e);
		}
	}	
	/**
	 * 识别 判读是否存储 推送JSON数据
	 * @param switchstr
	 * @param info
	 * @throws JSONException 
	 */
	public  static void saveInfoJson(final JSONObject info) throws JSONException{
			boolean tag = true;
			String[] args={"B","info"}; 
			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
			Cursor result=db.query("receiver_info", new String[] {"B","info"}, null,null,null,null,null);//全查询
//			条件查询
//			Cursor result=db.query("receiver_info", new String[] {"B","info"},"info=?", new String[]{String.valueOf(info)}, null,null, null, null);
			while (result.moveToNext()) {
				String tempStr=result.getString(result.getColumnIndex("info"));
				if(!AppUtil.isEmpty(tempStr)){
					// 处理相同数据
					if(info.toString()!=null  && info.toString().startsWith(tempStr)){
						tag=false;
//						//删除已经显示过的消息(已经显示过的消息 B 字段为 1 ) 和重复的消息
		    			db.execSQL("delete from receiver_info  where info='"+info.toString()+"';");
		    			break;
				 }
				}
			}
			if(tag){
				if(!info.isNull("A")){
					//插入推送消息数据   设置为未读消息 04  05 类消息不存储
					if(!"04".endsWith(info.getString("A"))&& !"05".endsWith(info.getString("A")) ){
						ContentValues cv = new ContentValues();
						cv.put("info", String.valueOf(info));
						cv.put("B", 0);
						db.insert("receiver_info", null, cv);
						System.out.println("info----------------->insert");
					}
				}
			}
					
	}
	
}
