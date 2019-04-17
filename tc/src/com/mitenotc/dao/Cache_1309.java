package com.mitenotc.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import com.mitenotc.net.MessageJson;
import com.mitenotc.tc.MyApp;
import com.mitenotc.ui.account.AccountUtils;
import com.mitenotc.utils.FormatUtil;

public class Cache_1309 implements BaseCache {
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
				db.execSQL("delete from ImageCache_1309");
			for (int i = 0; i < list.length(); i++) {
				ContentValues cv = new ContentValues();
				JSONObject obj = list.getJSONObject(i);
				if (isEndTime(obj.getString("B"))) {
					// 删除过期的图片
					filecache.removeCache(DigestUtils.md5Hex(obj.getString("A")));
				} else {
					cv.put("A", obj.getString("A"));
					cv.put("B", obj.getString("B"));
					String filename = DigestUtils.md5Hex(obj.getString("A"));
					
					if (!fileExist(filename)) {
						MyApp.loadimageList.add(obj.getString("A"));
					}

					db.insert("ImageCache_1309", null, cv);
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

	public Bitmap getPicture() {
		SQLiteDatabase db = MyApp.datacache.getWritableDatabase();
		if (db == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Cursor cursorInfo = db.rawQuery("select A,B from ImageCache_1309", null);
		cursorInfo.getCount();
		while (cursorInfo.moveToNext()) {
			String time = cursorInfo.getString(1);
			if (!isEndTime(time)&& fileExist(DigestUtils.md5Hex(cursorInfo.getString(0)))){
				list.add(cursorInfo.getString(0));
			}
		}
		cursorInfo.close();
		if (list.size() > 0) {
			Random obj = new Random();
			int index = obj.nextInt(list.size());
			String filename = DigestUtils.md5Hex(list.get(index));
			try {
				return filecache.getImageFromFile1(filename);
			} catch (Exception e) {
				return null;
			}
			
		} else {
			return null;
		}
	}
}
