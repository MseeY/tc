package com.mitenotc.dao;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{
	public static String NAME = "miteno.db";
	public static int VERSION = 1;
	public DBHelper(Context context) {
		super(context, NAME,null,VERSION);
	}

	public static final String TABLE_ID = "_id";
	public static final String HALL_ITEM_TABLE_NAME = "hall_item";
	public static final String HALL_ITEM_ICONPATH = "iconPath";
	public static final String HALL_ITEM_TITLE = "title";
	public static final String HALL_ITEM_DESC = "desc";
	public static final String HALL_ITEM_ISSUE = "issue";
	public static final String HALL_ITEM_PRIZEICONS = "prizeIcons";
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+HALL_ITEM_TABLE_NAME+" (_id integer primary key,"
				+HALL_ITEM_ICONPATH + " varchar(100), "  //
				+HALL_ITEM_TITLE + " varchar(100), "  //
				+HALL_ITEM_DESC + " varchar(100), "  //
				+HALL_ITEM_ISSUE + " varchar(100), "  //
				+HALL_ITEM_PRIZEICONS + " varchar(100)"  //
				+")"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
