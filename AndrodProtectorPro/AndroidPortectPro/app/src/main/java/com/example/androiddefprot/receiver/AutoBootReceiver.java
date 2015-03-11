package com.example.androiddefprot.receiver;

import com.example.androiddefprot.ui.SplashActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *
 * @Title: AutoBootReceiver.java
 * @Package com.example.androiddefprot.receiver
 * @Description: 开机自动启动
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AutoBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent autoBootIntent = new Intent(context,SplashActivity.class);
		autoBootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//BroadcastReceiver,Serviceû������ջ������Ҫָ��һ���µ�ջ����Ȼ�ᱨ��
		context.startActivity(autoBootIntent);
	}

}
