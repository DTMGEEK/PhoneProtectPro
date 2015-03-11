package com.example.androiddefprot.engine;


import com.example.androiddefprot.dao.AddressDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 *
 * @Title: NumberAddressService.java
 * @Package com.example.androiddefprot.engine
 * @Description: 号码归属地业务类，链接数据库，并查询
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class NumberAddressService {
	
	public static String getAddressByNumber(String number){
		String pattern = "1[3458]\\d{9}$";
		String address = null;
		if(number.matches(pattern)){
		
			SQLiteDatabase db = AddressDao.getAddressDB("/sdcard/address.db");
			String sql = "select city from info where mobileprefix = ?";
			String[] params = new String[]{
				number.substring(0,7)    //切割出号码前7位，来判断是哪一个地区的
			};
			Cursor cursor = db.rawQuery(sql, params);
			while(cursor.moveToNext()){
				address = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}else{
			SQLiteDatabase db = null;
			switch(number.length()){
				case 4://4位号码
					address = "模拟器";
				break;
				
				case 7://7位号码
					address = "本地号码";
				break;
				
				case 8://8位号码
					address = "本地号码";
				break;
				
				case 10://10位号码
					db = AddressDao.getAddressDB("/sdcard/address.db");
					String sql = "select city from info where area = ? limit 1";
					String[] params = new String[]{
						number.substring(0,4)
					};
					Cursor cursor = db.rawQuery(sql, params);
					while(cursor.moveToNext()){
						address = cursor.getString(0);
					}
					cursor.close();
					db.close();
				break;
				
				case 11://11位号码
					db = AddressDao.getAddressDB("/sdcard/address.db");
					String sqls = "select city from info where area = ? limit 1";
					String[] paramss = new String[]{
							number.substring(0, 3)
					};
					Cursor cursors = db.rawQuery(sqls, paramss);
					while(cursors.moveToNext()){
						address = cursors.getString(0);
					}
					
					String sqlf= "select city from info where area = ? limit 1";
					String[] paramf = new String[]{
							number.substring(0, 4)
					};
					
					Cursor cursorf = db.rawQuery(sqls, paramf);
					while(cursorf.moveToNext()){
						address = cursorf.getString(0);
					}
					cursors.close();
					cursorf.close();
					db.close();
				break;
				
				case 12://12位号码
					SQLiteDatabase dbf = AddressDao.getAddressDB("/sdcard/address.db");
					String sqlfi = "select city from info where area = ? limit 1 ";
					String[] paramfi = new String[]{
						number.substring(0, 4)
					};
					Cursor cursorfi = dbf.rawQuery(sqlfi, paramfi);
					while(cursorfi.moveToNext()){
						address = cursorfi.getString(0);
					}
					cursorfi.close();
					dbf.close();
				break;
			}
		}
		return address;
		
		
	}

}
