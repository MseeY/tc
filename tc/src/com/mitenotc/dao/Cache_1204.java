//package com.mitenotc.dao;
//
//import java.text.SimpleDateFormat;
//import java.util.Locale;
//
//import org.apache.commons.codec.digest.DigestUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.mitenotc.net.MessageJson;
//import com.mitenotc.tc.MyApp;
//
//public class Cache_1204 implements BaseCache {
//
//	public Cache_1204() {
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void setCache(int cmd, MessageJson quest, MessageJson response) {
//		
//		// TODO Auto-generated method stub
//		
//		try 
//		{
//			if(Integer.valueOf(response.getString("A")) != 0 )
//						return;
//						
//			String hashkey = "";
//			if(quest != null)
//				hashkey = DigestUtils.md5Hex(quest.toString());
//			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
//			//db.insert("mitenotc_cache", nullColumnHack, values)
//			String[] selectionArgs={String.valueOf(cmd),hashkey};
//			
////			Cursor result= db.rawQuery("select validtime,responsestr from mitenotc_cache where cmd=? and requeststr=?", selectionArgs);
//			String[] outcloums = {"validtime"};
//			Cursor result= db.query("mitenotc_cache", outcloums, "cmd=? and requeststr=?", selectionArgs, null, null, null);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.CHINA);
//			long validtime = System.currentTimeMillis()+8*60*1000;
////			long validtime = sdf.parse(response.getString("D")).getTime()+7*60*1000;
//			if(result.getCount()>0)
//		    {
//		    	 ContentValues cv = new ContentValues();
//		    	 cv.put("responsestr", response.toString());
//		    	 cv.put("validtime", validtime);
//		    	 cv.put("addtime", System.currentTimeMillis());
//		    	 
//		    	db.update("mitenotc_cache", cv, "cmd=? and requeststr=?", selectionArgs);
//		    	////System.out.println("1204=====>setCache===update:"+hashkey);
//		    }else
//		    {
//		    	//insert 
//		    	 ContentValues cv = new ContentValues();
//		    	 cv.put("responsestr", response.toString());
//		    	 cv.put("validtime", validtime);
//		    	 cv.put("addtime", System.currentTimeMillis());
//		    	 cv.put("cmd", cmd);
//		    	 cv.put("requeststr", hashkey);		    	 
//		    	 ////System.out.println("1204=====>setCache===insert:"+hashkey);
//		    	db.insert("mitenotc_cache", null, cv);
//		    }
//		    result.close();
//		} catch (Exception e) 
//		{
//		}
//		
//	}
//	@Override
//	public MessageJson getCache(int cmd, MessageJson quest, int isforce) 
//	{	
//		////System.out.println("1204=====>getCache===Q:");
//		String hashkey = "";
//		String responsestr = null;
//		if(quest != null)
//			hashkey = DigestUtils.md5Hex(quest.toString());
//		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
//		if(db == null)
//			return null;
//		////System.out.println("1204=====>getCache===Q:"+hashkey);
//		String[] selectionArgs={String.valueOf(cmd),hashkey};
//		String[] outcloums = {"validtime","responsestr","addtime"};
//		Cursor result= db.query("mitenotc_cache", outcloums, "cmd=? and requeststr=?", selectionArgs, null, null, null);
//		result.moveToFirst();
//		long addtime = 0;
//	    while (!result.isAfterLast()) 
//	    {
//	    	long validtime = result.getLong(0);
//	    	responsestr = result.getString(1); 
//	    	addtime = result.getLong(2);////System.out.println("validtime - System.currentTimeMillis() === "+(validtime - System.currentTimeMillis()));
//	        if(validtime < System.currentTimeMillis() && isforce == 0)
//	        {
//	        	responsestr = null;
//	        }
//	        result.moveToNext();
//	     }
//	      result.close();
//	      ////System.out.println("1204=====>getCache===Q:"+responsestr);
//	    if(responsestr == null)
//	    			return null;
//		else
//			try {
//				MessageJson resultjson = new MessageJson(responsestr);
//				return resultjson;
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				return null;
//			}
//	}
//
//}
