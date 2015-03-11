package com.example.androiddefprot.test;

import com.example.androiddefprot.engine.ContactInfoService;

import android.test.AndroidTestCase;

/*
 * 测试
 */
public class ContactInfoTest extends AndroidTestCase {

	
	public void getContactInfo() throws Exception{
		ContactInfoService service = new ContactInfoService();
		service.getContactInfos();
	}
	
}
