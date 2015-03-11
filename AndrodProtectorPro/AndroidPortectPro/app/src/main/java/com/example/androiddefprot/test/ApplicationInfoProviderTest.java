package com.example.androiddefprot.test;

import java.util.List;

import com.example.androiddefprot.domain.AppInfo;
import com.example.androiddefprot.engine.AppInfoProvider;

import android.test.AndroidTestCase;

/*
 * 测试
 */
public class ApplicationInfoProviderTest extends AndroidTestCase {

	public void ApplicationProviderTest(){
		AppInfoProvider provider = new AppInfoProvider(this.getContext());
		List<AppInfo> list = provider.getAllApp();
		System.out.println(list.size());
	}
	
	
}
