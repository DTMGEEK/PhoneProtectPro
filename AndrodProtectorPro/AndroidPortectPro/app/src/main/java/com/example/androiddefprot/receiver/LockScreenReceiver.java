package com.example.androiddefprot.receiver;

import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.util.TaskUitl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

/**
 *
 * @Title: LockScreenReceiver.java
 * @Package com.example.androiddefprot.receiver
 * @Description: 锁屏广播接收器
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class LockScreenReceiver extends BroadcastReceiver {

	private static final String TAG = "LockScreenReceiver";
	

	@Override
	public void onReceive(Context context, Intent intent) {
		
		LogManagement.i(TAG, "锁屏自动清理内存");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean killprocess = sp.getBoolean("killprocess", false);
		//�������������Զ������ڴ�
		if(killprocess){
			TaskUitl.killAllProcess(context);
			LogManagement.i(TAG, "杀死所有进程");
		}

	}

}
