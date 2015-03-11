package com.example.androiddefprot.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 *
 * @Title: AppLockHelper.java
 * @Package com.example.androiddefprot.db
 * @Description: 程序锁数据库dao类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppLockHelper extends SQLiteOpenHelper {
	
	private static final String DATABASENAME = "applock.db";  //数据库名称
	private static final String TABLENAME = "applock";		  //数据表名称
	private static final int DBVERSION = 1;					//数据库版本号

	
	public AppLockHelper(Context context){
		super(context,DATABASENAME,null,DBVERSION);         
	}
	
	
	//��ϵͳ��û�г�������ݿ��ʱ����ã�����һ��������ݿ�
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table " + this.TABLENAME + "("
	                + "_id integer primary key ,"
	                + "packagename varchar(20)"
					+ ")";
		db.execSQL(sql);
	}


    //当系统中没有程序锁数据库的时候调用，创建一个黑名单数据库
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql = "drop table if exists " + this.TABLENAME ;
		db.execSQL(sql);
		this.onCreate(db);
	}

}
