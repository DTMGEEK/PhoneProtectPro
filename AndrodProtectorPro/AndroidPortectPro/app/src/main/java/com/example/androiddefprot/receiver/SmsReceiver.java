package com.example.androiddefprot.receiver;


import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.GPSInfoProvider;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.telephony.gsm.SmsManager;
import android.util.Log;

/**
 *
 * @Title: SmsReceiver.java
 * @Package com.example.androiddefprot.receiver
 * @Description: 短信广播接收器 ，读取短信内容，进行相应的操作。
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			
			String content = sms.getMessageBody();
			String sender = sms.getOriginatingAddress();
			LogManagement.i(TAG, "短信内容："+content);
			if("#*location*#".equals(content)){
				this.abortBroadcast();
				GPSInfoProvider provider = GPSInfoProvider.getInstance(context);
				String location = provider.getLocation();
				if("".equals(location)){
					
				}else{
					SmsManager manager = SmsManager.getDefault();
					manager.sendTextMessage(sender, null, "地点是:"+location, null, null);
				}
			
			}else if("#*locknow*#".equals(content)){    //远程控制锁屏，并加设密码。
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.resetPassword("123", 0);
				manager.lockNow();
				this.abortBroadcast();
			}else if("#*wipedata*#".equals(content)){       //发送短信把手机的数据完全清空
				DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
				manager.wipeData(0);
				this.abortBroadcast();
			}else if("#*alarm*#".equals(content)){        //发送短信使手机发出报警音
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1.0f, 1.0f);
				player.start();
				this.abortBroadcast();
			}
			
		}
	}

}
