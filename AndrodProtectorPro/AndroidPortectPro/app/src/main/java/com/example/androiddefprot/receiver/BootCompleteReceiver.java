package com.example.androiddefprot.receiver;

import com.example.androiddefprot.logmanagement.LogManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

/**
 *
 * @Title: BootCompleteReceiver.java
 * @Package com.example.androiddefprot.receiver
 * @Description: 当手机重启后，监听sim卡的串号是否有改变，改变了就向预先设置的安全手机发送短信
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	private SharedPreferences sp = null;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
	    LogManagement.i(TAG, "�������");
		  
		this.sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isprotecting = this.sp.getBoolean("isprotecting", false);
		if(isprotecting){
			TelephonyManager telephonyManager =  (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String simNumber = telephonyManager.getSimSerialNumber();
			String realSim = sp.getString("sim", "");
	
			if(!simNumber.equals(realSim)){
				LogManagement.i(TAG, "重启完毕");
				SmsManager smsManager = SmsManager.getDefault();
				String destinationAddress = this.sp.getString("safenumber", "");
				smsManager.sendTextMessage(destinationAddress, null, "手机sim卡 改变，可能被盗", null, null);
			}
		}
	}

}
