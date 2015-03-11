package com.example.androiddefprot.receiver;

import com.example.androiddefprot.ui.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 *
 * @Title: CallPhoneReceiver.java
 * @Package com.example.androiddefprot.receiver
 * @Description: 拨号界面特征码，监听，当监听到拨打123123时就会进入到 手机卫士的防盗界面
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class CallPhoneReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = this.getResultData();
		if("123123".equals(number)){
			Intent lostIntent = new Intent(context,LostProtectedActivity.class);
            //指定要激活的activity在自己的任务栈里面运行
			lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     //在Activity任务栈外面调用Activity，激活的Activity必须运
			context.startActivity(lostIntent);
            // 终止掉这个电话
            // 不能通过  abortBroadcast();
			this.setResultData(null); //终止拨打电话
		}
	}

}
