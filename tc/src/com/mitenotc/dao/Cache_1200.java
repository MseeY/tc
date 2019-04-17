package com.mitenotc.dao;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;

public class Cache_1200 implements BaseCache {

	public Cache_1200() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void setCache(int cmd, MessageJson quest, MessageJson response) {
		
		// TODO Auto-generated method stub
		
		try 
		{
			if(Integer.valueOf(response.getString("A")) != 0 )
						return;
						
			String hashkey = "";
			if(quest != null)
				hashkey = DigestUtils.md5Hex(quest.toString());
			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
			//db.insert("mitenotc_cache", nullColumnHack, values)
			String[] selectionArgs={String.valueOf(cmd),hashkey};
			
//			Cursor result= db.rawQuery("select validtime,responsestr from mitenotc_cache where cmd=? and requeststr=?", selectionArgs);
			String[] outcloums = {"validtime"};
			Cursor result= db.query("mitenotc_cache", outcloums, "cmd=? and requeststr=?", selectionArgs, null, null, null);
			long outtime=System.currentTimeMillis();
			JSONArray list = response.getJSONArray("LIST");
			for(int i=0;i<list.length();i++)
			{
				if(outtime > list.getJSONObject(i).getLong("G"))
				{
					outtime = list.getJSONObject(i).getLong("G");
				}
			}
			long validtime = outtime*1000 + System.currentTimeMillis();
			if(result.getCount()>0)
		    {
		    	//update
		    	 ContentValues cv = new ContentValues();
		    	 cv.put("responsestr", response.toString());
		    	 cv.put("validtime", validtime);
		    	 cv.put("addtime", System.currentTimeMillis());
		    	 
		    	db.update("mitenotc_cache", cv, "cmd=? and requeststr=?", selectionArgs);
		    	////System.out.println("1200=====>setCache===update:"+hashkey);
		    }else
		    {
		    	//insert 
		    	 ContentValues cv = new ContentValues();
		    	 cv.put("responsestr", response.toString());
		    	 cv.put("validtime", validtime);
		    	 cv.put("addtime", System.currentTimeMillis());
		    	 cv.put("cmd", cmd);
		    	 cv.put("requeststr", hashkey);		    	 
		    	 ////System.out.println("1200=====>setCache===insert:"+hashkey);
		    	db.insert("mitenotc_cache", null, cv);
		    }
		    result.close();
		} catch (Exception e) 
		{
		}
		
	}
	@Override
	public MessageJson getCache(int cmd, MessageJson quest, int isforce) 
	{	
		////System.out.println("1200=====>getCache===Q:");
		String hashkey = "";
		String responsestr = null;
		if(quest != null)
			hashkey = DigestUtils.md5Hex(quest.toString());
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if(db == null)
			return null;
		////System.out.println("1200=====>getCache===Q:"+hashkey);
		String[] selectionArgs={String.valueOf(cmd),hashkey};
		String[] outcloums = {"validtime","responsestr","addtime"};
		Cursor result= db.query("mitenotc_cache", outcloums, "cmd=? and requeststr=?", selectionArgs, null, null, null);
		result.moveToFirst();
		long addtime = 0;
	    while (!result.isAfterLast()) 
	    {
	    	long validtime = result.getLong(0);
	    	responsestr = result.getString(1); 
	    	addtime = result.getLong(2);
	        if(validtime < System.currentTimeMillis() && isforce == 0)
	        {
	        	responsestr = null;
	        }
	        result.moveToNext();
	     }
	      result.close();
	      ////System.out.println("1200=====>getCache===Q:"+responsestr);
	    if(responsestr == null)
	    			return null;
		else
			try {
				MessageJson resultjson = new MessageJson(responsestr);
				JSONArray list = resultjson.getJSONArray("LIST");
				for(int i=0;i<list.length();i++)
				{
					long G = list.getJSONObject(i).getLong("G") - (System.currentTimeMillis()-addtime)/1000;
					
					list.getJSONObject(i).put("G", G < 0 ? 0: G);					
				}
				return resultjson;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return null;
			}
	}
}
