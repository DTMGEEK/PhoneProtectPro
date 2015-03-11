package com.example.androiddefprot.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androiddefprot.db.BlackNumberHelper;


/**
 *
 * @Title: BlackNumberDao.java
 * @Package com.example.androiddefprot.db.dao
 * @Description: 黑名单DAO操作类
 * @author lian_weijian@163.com
 * @version V1.0
 */

public class BlackNumberDao {
	
	private Context context = null;
	private BlackNumberHelper helper = null;
	private SQLiteDatabase db = null;
	
	
	public BlackNumberDao(Context context) {
		super();
		this.context = context;
		this.helper = new BlackNumberHelper(context);
	}


    /**
     * 插入黑名单数据
     * @param number 用户输入要查询的号码
     * @return
     */
	public boolean find(String number){
		boolean flag = false;
		db = helper.getReadableDatabase();
		if(db.isOpen()){
			
			String sql = "select number from blacknumber where number = ?";
			String[] args = new String[]{number};
			Cursor cursor = db.rawQuery(sql, args);
			while(cursor.moveToNext()){
				flag = true;
			}
			cursor.close();
			db.close();
		}
		return flag;
	}




    /**
     * 增加黑名单号码
     * @param number
     */
	public void add(String number){
		if(find(number)){
			return;
		}
		
		db = helper.getWritableDatabase();
		if(db.isOpen()){
			String sql = "insert into blacknumber(number) values (?)";
			String[] args = new String[]{number};
			db.execSQL(sql,args);
			db.close();
		}
	}




    /**
     * 删除黑名单
     * @param number
     */
	public void delete(String number){
		db = helper.getWritableDatabase();
		if(db.isOpen()){
			String sql = "delete from blacknumber where number = ?";
			String[] args = new String[]{number};
			db.execSQL(sql,args);
			db.close();
		}
	}



    /**
     * 修改黑名单电话号码
     * @param number
     */
	public void update(String oldNumber,String newNumber){
		db = helper.getWritableDatabase();
		if(db.isOpen()){
			String sql = "update blacknumber set number = ? where number = ?";
			String[] args = new String[]{newNumber,oldNumber};
			db.execSQL(sql, args);
			db.close();
		}
	}


    /**
     * 查询出所有的黑名单电话号码
     * @return
     */
	public List<String> getAllNumber(){
		db = helper.getReadableDatabase();
		List<String> numbers = null;
		if(db.isOpen()){
			numbers = new ArrayList<String>();
		    String sql = "select number from blacknumber";
		    Cursor cursor = db.rawQuery(sql, null);
		    while(cursor.moveToNext()){
		    	numbers.add(cursor.getString(0));
		    }
		    cursor.close();
		    db.close();
		}
		return numbers;
	}
	
	
}
