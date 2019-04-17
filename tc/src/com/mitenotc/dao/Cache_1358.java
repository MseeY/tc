package com.mitenotc.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mitenotc.bean.MessageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.utils.FormatUtil;

public class Cache_1358 implements BaseCache {
    
	 public  static int MSG_COUNT=0;// 客服信息/客服留言 条目
	@Override
	public void setCache(int cmd, MessageJson quest, MessageJson response) {
		// TODO Auto-generated method stub
		
		if("0".equals(response.getString("A")))
		{
			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
			if(db == null)
				return ;
			JSONArray list;
			try {
				if(response.isNull("LIST"))
					return;
				list = response.getJSONArray("LIST");
				MSG_COUNT += list.length();
				////System.out.println("c-----------------list.length() :"+list.length());
			for(int i=0;i<list.length();i++)
			{
				
		    	 ContentValues cv = new ContentValues();
		    	 cv.put("A", "2");  //消息来源
		    	 cv.put("B", list.getJSONObject(i).getString("D")); //B 消息内容
		    	 cv.put("C", list.getJSONObject(i).getString("A")); //C 消息时间 , YYYY-MM-DD HH:MI:SS
		    	 cv.put("D", "0");                     // D  提交状态，A==1时，用户的消息是否成功提交到服务端,0成功，1失败
		    	 cv.put("E", response.getString("C"));  //E  用户名称，当A=1时，用户名称为空
		    	 cv.put("addtime", FormatUtil.timeFormat(list.getJSONObject(i).getString("A")));		    	 
		    	 db.insert("mitenotc_cache_1353", null, cv);			
			}
			} catch (JSONException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		////System.out.println("Cache_1358()===setCache："+response.toString());
	}

	@Override
	public MessageJson getCache(int cmd, MessageJson quest, int isforce) {
		// TODO Auto-generated method stub
		return null;
	}

	public static int getMSG_COUNT() 
	{
		////System.out.println("count--------------->"+MSG_COUNT);
		return MSG_COUNT;
	}
}
