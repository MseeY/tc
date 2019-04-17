package com.mitenotc.dao.impl;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDaoImpl {
	
	private SQLiteDatabase db;
	
	public static final String path = "/data/data/com.mitenotc.tc/files/city.db";

	public List<String> findProvince() {
		db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.rawQuery("select * from city t where substr(t.id,6,10)='0000'",null);
		List<String> list=new ArrayList<String>();
		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		db.close();
		return list;
	}
	
//	select *
//	  from city a
//	 where substr(a.id, 0, 6) = 
//	 
//	 (select substr(b.id, 0, 6)
//	     from city b where b.name='北京'and substr(b.id, 6, 10)='0000') and substr(a.id, 6, 10)<>'0000' ;   
	
	public List<String> findCityByName(String provName) {
		
		db = SQLiteDatabase.openDatabase(path, null,SQLiteDatabase.OPEN_READONLY);
		
		Cursor cursor = db.rawQuery("select * from city a where substr(a.id, 0, 6)=(select substr(b.id, 0, 6)from city b where b.name=? and substr(b.id, 6, 10)='0000') and substr(a.id, 6, 10)<>'0000'",new String[]{provName});
		List<String> list=new ArrayList<String>();
		while(cursor.moveToNext()){
			list.add(cursor.getString(cursor.getColumnIndex("name")));
		}
		cursor.close();
		db.close();
		return list;
	}
}


