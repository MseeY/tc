package com.mitenotc.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CacheDBHelper extends SQLiteOpenHelper {
	public static String NAME = "cachedb.db";
	public static int VERSION = 5;
	private SQLiteDatabase db; 
	 public CacheDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
		@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		// TODO Auto-generated method stub
		return super.getReadableDatabase();
	}
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		// TODO Auto-generated method stub
		return super.getWritableDatabase();
	}
		public CacheDBHelper(Context context) {
			//CursorFactory设置为null,使用默认值  
			super(context, NAME, null, VERSION);
			db = this.getWritableDatabase();
		}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS  mitenotc_cache (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
				+"cmd integer," 
				+"requeststr varchar,"
				+"responsestr text,"
				+"validtime long," // 1970的秒数
				+"addtime long" // 1970的秒数
				+")");
		
		//1353用户会话表
		db.execSQL("CREATE TABLE IF NOT EXISTS  mitenotc_cache_1353 (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
				+"A integer," 
				+"B text,"
				+"C varchar,"
				+"D varchar,"
				+"E varchar,"
				+"addtime long"     // 1970的秒数
				+")");
		//1350资讯
		db.execSQL("CREATE TABLE IF NOT EXISTS mitenotc_cache_1350 (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
				+"A integer," 
				+"B text,"
				+"C varchar,"
				+"D varchar,"
				+"E varchar,"
				+"F varchar,"
				+"addtime long"     // 1970的秒数
				+")");
		//探针信息
		db.execSQL("CREATE TABLE IF NOT EXISTS  probemsg (_id INTEGER PRIMARY KEY  AUTOINCREMENT,"
				+"msg text )");// MessageJson.toString
		// 推送消息 通知
 		db.execSQL("CREATE TABLE IF NOT EXISTS receiver_info (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
 				+"B integer,"      // 是否已读 0未读  1 
 				+"info varchar(100) )");
 		
		db.execSQL("CREATE TABLE IF NOT EXISTS  ImageCache_1308 (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
				+"A varchar," 
				+"B varchar,"
				+"C varchar,"
				+"D varchar,"
				+"E varchar"
				+")");
		db.execSQL("CREATE TABLE IF NOT EXISTS  ImageCache_1309 (_id INTEGER PRIMARY KEY   AUTOINCREMENT,"
				+"A varchar," 
				+"B varchar"
				+")");
	}	 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("new -- newVersion"+newVersion);
		System.out.println("new -- oldVersion"+oldVersion);
         if (oldVersion != VERSION) {// 版本不同更新数据库
        	 System.out.println("new -----newVersion------>"+newVersion);
        	 System.out.println("new -----oldVersion------>"+oldVersion);
        	 System.out.println("new -------VERSION----->"+VERSION);
        	  db.execSQL("DROP TABLE IF EXISTS mitenotc_cache");  
        	  db.execSQL("DROP TABLE IF EXISTS mitenotc_cache_1353");  
        	  db.execSQL("DROP TABLE IF EXISTS mitenotc_cache_1350");  
        	  
        	  db.execSQL("DROP TABLE IF EXISTS probemsg");
        	  db.execSQL("DROP TABLE IF EXISTS receiver_info");
        	  db.execSQL("DROP TABLE IF EXISTS ImageCache_1308");  
        	  db.execSQL("DROP TABLE IF EXISTS ImageCache_1309");  
             onCreate(db);
         }
	}
	
	

}
