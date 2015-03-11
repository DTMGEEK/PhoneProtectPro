package com.example.androiddefprot.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.db.AppLockHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 *
 * @Title: AppLockDao.java
 * @Package com.example.androiddefprot.db.dao
 * @Description: 应用程序所DAO类实现CRUD操作
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppLockDao {
	
	private Context context = null;
	private AppLockHelper helper = null;
	private SQLiteDatabase db = null;
	
	
	public AppLockDao(Context context) {
		super();
		this.context = context;
		this.helper = new AppLockHelper(context);
	}


    /**
     * 查询是否有这个包
     * @param packagename 包名
     * @return 返回true（有这个包名）/false（没有这个包名）
     */
	public boolean find(String packagename){
		boolean flag = false;
		this.db = this.helper.getReadableDatabase();
		if(db.isOpen()){
		  String sql = "select packagename from applock where packagename = ?";
		  String[] args = new String[]{packagename};
		  Cursor cursor = this.db.rawQuery(sql, args);
		  while(cursor.moveToNext()){
			 flag = true;
		  }
		  cursor.close();
		  this.db.close();
		}
		return flag;
	}



    /**
     * 增加包名到数据库
     * @param packagename 包名
     */
	public void add(String packagename){
		db = this.helper.getWritableDatabase();
		if(db.isOpen()){
			String sql = "insert into applock(packagename) values(?)";
			String[] args = new String[]{packagename};
			db.execSQL(sql, args);
			db.close();
		}
	}



    /**
     * 删除包名
     * @param packagename 包名
     */
	public void delete(String packagename){
		this.db = this.helper.getWritableDatabase();
		if(db.isOpen()){
			String sql = "delete from applock where packagename = ?";
			String[] args = new String[]{packagename};
			db.execSQL(sql, args);
			db.close();
		}
	}



    /**
     * 查询所有的包
     * @return 包含所有包名的List集合
     */
	public List<String> findAll(){
		List<String> infos = new ArrayList<String>();
		this.db = this.helper.getReadableDatabase();
		if(db.isOpen()){
		  String sql = "select packagename from applock";
		  Cursor cursor = this.db.rawQuery(sql, null);
		  while(cursor.moveToNext()){
			  infos.add(cursor.getString(cursor.getColumnIndex("packagename")));
		  }
		  cursor.close();
		  this.db.close();
		}
		return infos;
	}
	
	

}
