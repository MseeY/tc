package com.mitenotc.dao;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.utils.FormatUtil;

public class Cache_1353 implements BaseCache {

	public Cache_1353() {
	}

	@Override
	public void setCache(int cmd, MessageJson quest, MessageJson response) {
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if(db == null)
			return ;

	if("0".equals(response.getString("A")))
	{
		if(!quest.isNull("B"))
		{
			ContentValues cv = new ContentValues();
			cv.put("A", "1");
			cv.put("B", quest.getString("B"));
			cv.put("C", FormatUtil.timeFormat(System.currentTimeMillis()));
			cv.put("D", "0");
			cv.put("E", "user");
			cv.put("addtime", FormatUtil.timeFormat(response.getString("C")));		
			db.insert("mitenotc_cache_1353", null, cv);
		}
		JSONArray list;
		try {
			if(response.isNull("LIST"))
				return;
			list = response.getJSONArray("LIST");
		
		for(int i=0;i<list.length();i++)
		{
			
	    	 ContentValues cv = new ContentValues();
	    	 cv.put("A", "2");  //消息来源
	    	 cv.put("B", list.getJSONObject(i).getString("D")); //B 消息内容
	    	 cv.put("C", list.getJSONObject(i).getString("A")); //C 消息时间 , YYYY-MM-DD HH:MI:SS
	    	 cv.put("D", "0");                     // D  提交状态，A==1时，用户的消息是否成功提交到服务端,0成功，1失败
	    	 cv.put("E", response.getString("C"));  //E  用户名称，当A=1时，用户名称为空
	    	 cv.put("addtime", FormatUtil.timeFormat(list.getJSONObject(i).getString("A"))+100);//  --TODO 2014-8-6 万利修改		
	    	 db.insert("mitenotc_cache_1353", null, cv);		
		}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
	}
	////System.out.println("Cache_1353()===setCache："+response.toString());
}

	@Override
	public MessageJson getCache(int cmd, MessageJson quest, int isforce) {
/*		if(isforce==0)
		{
			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
			if(db == null)
				return null;
			if(!quest.isNull("B"))
			{
				ContentValues cv = new ContentValues();
				cv.put("A", "1");
				cv.put("B", quest.getString("B"));
				cv.put("C", FormatUtil.timeFormat(System.currentTimeMillis()));
				cv.put("D", "0");
				cv.put("E", "user");
				cv.put("addtime", System.currentTimeMillis());		    	 
				db.insert("mitenotc_cache_1353", null, cv);
			}
		}*/
		////System.out.println("Cache_1353()===getCache："+quest.toString());
		return null;
	}
	/* LIST
	 * A 消息属性，1 来自用户    2 来自客服务
	 * B 消息内容
	 * C 消息时间 , YYYY-MM-DD HH:MI:SS
	 * D  提交状态，A==1时，用户的消息是否成功提交到服务端,0成功，1失败
	 * E  用户名称，当A=1时，用户名称为空
	 * List<MessageBean>
	 * */
	public MessageBean getMsgList()
	{
		Cache_1358.MSG_COUNT=0;// 归零
		MessageBean msg = new MessageBean();
		List<MessageBean> list = new ArrayList<MessageBean>();	
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if(db == null)
			return null;
		String[] outcloums = {"A","B","C","D","E"};
		//Cursor result= db.query("mitenotc_cache_1353", outcloums, null, null, null, null, " order by addtime desc ");
		Cursor result= db.rawQuery("select A,B,C,D,E from mitenotc_cache_1353 order by addtime desc limit 100",null);
		result.moveToFirst();

	    while (!result.isAfterLast()) 
	    {
	    	MessageBean tmpmsg = new MessageBean();
	    	tmpmsg.setA(result.getString(0));
	    	tmpmsg.setB(result.getString(1));
	    	tmpmsg.setC(result.getString(2));
	    	tmpmsg.setD(result.getString(3));
	    	tmpmsg.setE(result.getString(4));
	    	list.add(tmpmsg);
 	        result.moveToNext();
	     }
	      result.close();
	      ////System.out.println("Cache_1353()===getMsgList："+list.size()+"ssssssss");
		msg.setLIST(list);
		return msg;
		}
}
