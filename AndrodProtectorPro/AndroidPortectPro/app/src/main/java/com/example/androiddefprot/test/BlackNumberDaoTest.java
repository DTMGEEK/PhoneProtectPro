package com.example.androiddefprot.test;

import java.util.List;

import com.example.androiddefprot.db.dao.BlackNumberDao;

import android.test.AndroidTestCase;

/*
 * 测试
 */
public class BlackNumberDaoTest extends AndroidTestCase {
	
	/*
	public void testAdd() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(this.getContext());
		String number = "135004567";
		for(int i = 0; i < 10; i++){
			dao.add("135004567" + i);
		}
	}*/
	
	
	/*
	public void testFind() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(this.getContext());
		boolean flag = dao.find("1350045670");
		System.out.println(flag);
	}*/
	
	
	
	
	/*public void testFindAll() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(this.getContext());
		List<String> all = dao.getAllNumber();
		System.out.print(all.size());
	}*/
	
	
	
	
	/*public void testUpdate() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(this.getContext());
		dao.update("1350045670", "13750042394");
	}*/

	
	
	public void testDelete() throws Exception{
		BlackNumberDao dao = new BlackNumberDao(this.getContext());
		dao.delete("13750042394");
	}
	
	
}
