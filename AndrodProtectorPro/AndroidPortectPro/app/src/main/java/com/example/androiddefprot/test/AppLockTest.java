package com.example.androiddefprot.test;

import com.example.androiddefprot.db.dao.AppLockDao;

import android.test.AndroidTestCase;

/*
 * 测试
 */
public class AppLockTest extends AndroidTestCase {
	
	public void AddText(){
		AppLockDao dao = new AppLockDao(this.getContext());
		dao.add("com.text.androiddefprot.xxx1");
		dao.add("com.text.androiddefprot.xxx2");
		dao.add("com.text.androiddefprot.xxx3");
		dao.add("com.text.androiddefprot.xxx4");
	}

}
