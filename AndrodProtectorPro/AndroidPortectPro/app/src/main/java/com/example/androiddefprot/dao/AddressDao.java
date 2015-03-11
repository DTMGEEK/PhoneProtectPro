package com.example.androiddefprot.dao;

import android.database.sqlite.SQLiteDatabase;

/**
 *
 * @Title: AddressDao.java
 * @Package com.example.androiddefprot.dao
 * @Description: 号码归属地查询DAO
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AddressDao {

	public static SQLiteDatabase getAddressDB(String path){
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	
}
