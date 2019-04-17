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

public class Cache_1350 implements BaseCache {

	public Cache_1350() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setCache(int cmd, MessageJson quest, MessageJson response) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if(db == null)
			return ;

	if("0".equals(response.getString("A")))
	{
		JSONArray list;
		try {
			if(response.isNull("LIST"))
				return;
			list = response.getJSONArray("LIST");
			if(list.length() <= 0)
				return ;
		for(int i=0;i<list.length();i++)
		{
	    	 ContentValues cv = new ContentValues();
	    	 cv.put("A", list.getJSONObject(i).getInt("A")); 
	    	 cv.put("B", list.getJSONObject(i).getString("B"));
	    	 cv.put("C", list.getJSONObject(i).getString("C"));
	    	 cv.put("D", list.getJSONObject(i).getString("D"));
	    	 cv.put("E", list.getJSONObject(i).getString("E"));
	    	 cv.put("F", list.getJSONObject(i).getString("F"));
	    	 cv.put("addtime",System.currentTimeMillis());
	    	 db.insert("mitenotc_cache_1350", null, cv);		
		}
		} catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	////System.out.println("Cache_1350()===setCache："+response.toString());
	}

	@Override
	public MessageJson getCache(int cmd, MessageJson quest, int isforce) {
		// TODO Auto-generated method stub
		MessageJson resultjson = new MessageJson();
		resultjson.put("A", "0");
		resultjson.put("B", "OK");
		JSONArray list = new JSONArray();  
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		
		if(db == null)
			return null;
		String spec = quest.isNull("A") ? "" : quest.getString("A");
		String nums = quest.isNull("B") ? "50" : quest.getString("B");
		//Cursor result= db.query("mitenotc_cache_1353", outcloums, null, null, null, null, " order by addtime desc ");
		Cursor result;
		if("".equals(spec))
			result= db.rawQuery("select A,B,C,D,E,F,addtime from mitenotc_cache_1350  order by addtime desc limit " + nums,null);
		else			
			result= db.rawQuery("select A,B,C,D,E,F,addtime from mitenotc_cache_1350 where F= "+ spec +" order by addtime desc limit " + nums,null);

		result.moveToFirst();
		if(result.getCount()<=0)
			{
				result.close();
				return null;
			}
		if(isforce==0 && !result.isAfterLast())
			if( result.getLong(6) + 1*60*60*1000 > System.currentTimeMillis() )
			{
				result.close();
				return null;
			}
	    while (!result.isAfterLast()) 
	    {
	    	MessageJson tmpmsg = new MessageJson();
	    	tmpmsg.put("A",result.getString(0));
	    	tmpmsg.put("B",result.getString(1));
	    	tmpmsg.put("C",result.getString(2));
	    	tmpmsg.put("D",result.getString(3));
	    	tmpmsg.put("E",result.getString(4));
	    	tmpmsg.put("F",result.getString(5));
	    	
	    	list.put(tmpmsg);
 	        result.moveToNext();
	     }
	      result.close();
	      ////System.out.println("Cache_1350()===getMsgList："+list.length()+"ssssssss");
	      resultjson.put("LIST", list);
		return resultjson;
	}
}
