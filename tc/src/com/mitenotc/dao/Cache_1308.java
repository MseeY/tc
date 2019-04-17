package com.mitenotc.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.mitenotc.bean.ImageBean;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.utils.FormatUtil;

public class Cache_1308 implements BaseCache {
	ImageFileCache filecache = new ImageFileCache();

	@Override
	public void setCache(int cmd, MessageJson quest, MessageJson response) {
		// TODO Auto-generated method stub

		if (Integer.valueOf(response.getString("A")) != 0)
			return;
		try {
			if (response.isNull("LIST"))
				return;
			SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
			if (db == null)
				return;
			JSONArray list = response.getJSONArray("LIST");
			if (list.length() > 0)
				db.execSQL("delete from ImageCache_1308");
			for (int i = 0; i < list.length(); i++) {
				ContentValues cv = new ContentValues();
				JSONObject obj = list.getJSONObject(i);
				if (isEndTime(obj.getString("C"))) {
					// 删除过期的图片
					filecache.removeCache(DigestUtils.md5Hex(obj.getString("B")));
				} else {
					cv.put("A", obj.getString("A"));
					cv.put("B", obj.getString("B"));
					cv.put("C", obj.getString("C"));
					cv.put("D", obj.getString("D"));
					cv.put("E", obj.getString("E"));
					String filename = DigestUtils.md5Hex(obj.getString("B"));
					if (!fileExist(filename)) {
						MyApp.loadimageList.add(obj.getString("B"));

					}

					db.insert("ImageCache_1308", null, cv);
				}
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 如果数据过期就根据过期
	 */
	public boolean isEndTime(String oldTime) {
		String nowTime = AccountUtils.getStringDateShort();
		long oldFormat = FormatUtil.dateFormat(oldTime);
		long nowFormat = FormatUtil.dateFormat(nowTime);
		return nowFormat - oldFormat > 0;// true为已过期
	}

	public boolean fileExist(String filename) {
		return filecache.fileExist(filename);
	}

	@Override
	public MessageJson getCache(int cmd, MessageJson quest, int isforce) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ImageBean> getListPic1308() {
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if (db == null) {
			return null;
		}
		
		List<ImageBean> list = new ArrayList<ImageBean>();
		Cursor cursorInfo = db.rawQuery("select A,B,C,D,E from ImageCache_1308", null);
		while (cursorInfo.moveToNext()) 
		{
			String time = cursorInfo.getString(2);
			if (!isEndTime(time)&& fileExist(DigestUtils.md5Hex(cursorInfo.getString(1)))) {
				try {
				System.out.println("cursorInfo.getString(1)  "+cursorInfo.getString(1));
				String filename = DigestUtils.md5Hex(cursorInfo.getString(1));
				Bitmap fileBitmap = filecache.getImageFromFile1(filename);
				ImageBean bean = new ImageBean();
				bean.setType(cursorInfo.getString(0));
				bean.setAddr(cursorInfo.getString(3));
				bean.setBm(fileBitmap);
				list.add(bean);
				} catch (Exception e) {
					return null;
				}
			}
		}

		cursorInfo.close();
		return list;
	}
}
