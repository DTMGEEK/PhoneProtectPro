package com.example.androiddefprot.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



/**
 *
 * @Title: BlackNumberHelper.java
 * @Package com.example.androiddefprot.db
 * @Description:黑名单功能数据库的数据库创建和更新类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class BlackNumberHelper extends SQLiteOpenHelper{
	
	private static final String DBNAME = "blacknumber.db";   //数据库名称
	private static final int DAVERSION = 1;					 //数据库版本号
	private static final String TABLENAME = "blacknumber";	 //数据表名称
	
	
	
	public BlackNumberHelper(Context context){
		super(context, DBNAME, null, DAVERSION);
		
	}



    //当系统中没有黑名单数据库的时候调用，创建一个黑名单数据库
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + TABLENAME 
				+ "("
				+ "_id integer primary key ,"
				+ "number varchar(20)"
				+ ")";
		db.execSQL(sql);
	}


    //数据库版本号更新后，重新生成数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "DROP TABLE IF EXISTS" + TABLENAME;
		db.execSQL(sql);
		this.onCreate(db);
		
	}

}
