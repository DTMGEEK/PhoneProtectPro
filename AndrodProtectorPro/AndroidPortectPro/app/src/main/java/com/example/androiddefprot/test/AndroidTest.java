package com.example.androiddefprot.test;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.UpdateInfo;
import com.example.androiddefprot.engine.UpdateInfoService;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.nfc.Tag;
import android.test.AndroidTestCase;
import android.util.Log;

/*
 * 测试
 */
public class AndroidTest extends AndroidTestCase {
	
	public void testUpdateInfoService() throws Exception{
		UpdateInfoService a = new UpdateInfoService(getContext());
		UpdateInfo info = a.getUpdateInfo(R.string.apkurl);
		System.out.println(info.getVersion());
		LogManagement.i("SplashActivity", info.getVersion());
	}
}
