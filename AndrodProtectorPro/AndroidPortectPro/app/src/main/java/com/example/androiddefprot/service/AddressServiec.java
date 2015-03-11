package com.example.androiddefprot.service;


import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.NumberAddressService;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: AddressServiec.java
 * @Package com.example.androiddefprot.service
 * @Description: 地址查询服务，监听手机来电的状态，当有电话打进来的时候就会查询数据库，得到来电的归属地
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AddressServiec extends Service {

	private static final String TAG = "AddressServiec";
	private TelephonyManager telephonyManager = null;     
	private WindowManager windowManager = null;
	private SharedPreferences sp = null;
	private View view = null;
	private MyPhoneStateListener listener = null;       //电话状态监听器，自定义的类继承了PhoneStateListener
	
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);//用于访问与手机通讯相关的状态和信息
		this.windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);         // 窗口控制类
		this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		listener = new MyPhoneStateListener();   
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE); //电话状态监听
	}


    //服务取消的时候调用
	@Override
	public void onDestroy() {
		super.onDestroy();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		this.view = null;
	}



    //电话状态监听
	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch(state){
			
			    case TelephonyManager.CALL_STATE_IDLE:  //电话处于静止状态，没有呼叫
			    	if(view != null){
			    		windowManager.removeView(view);     //去掉浮动的归属地显示卡，不然会长期停留在界面上
			    		view = null;

                        //再次获得系统时间，判断响铃是否超过3秒，如果不超过3秒，判断为骚扰电话
			    		
			    	}
				break;
			
				
			    case TelephonyManager.CALL_STATE_OFFHOOK:       //电话接通状态    //可以加入通话对方的当地天气状况
			    	if(view != null){
			    		windowManager.removeView(view);         //去掉浮动的归属地显示卡，不然会长期停留在界面上
			    		view = null;
			    	}
				break;
			
				
			    case TelephonyManager.CALL_STATE_RINGING:       //电话响铃状态
			    	LogManagement.i(TAG, "���������:" + incomingNumber);
			    	String address = NumberAddressService.getAddressByNumber(incomingNumber);
			    	//Toast.makeText(getApplicationContext(), "号码归属地是:" + address, Toast.LENGTH_LONG).show();
			    	LogManagement.i(TAG, "号码归属地是:" + address);
			    	showLocation(address);

                    //获取当前系统时间，拦截响一声的电话
			    	
				break;
			}
		}
	}



    //在窗体显示来电归属地信息
	public void showLocation(String address) {
        //设置控件布局的参数
		 WindowManager.LayoutParams params = new LayoutParams();    
	     params.height = WindowManager.LayoutParams.WRAP_CONTENT;
	     params.width = WindowManager.LayoutParams.WRAP_CONTENT;
	     params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
	                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
	     params.format = PixelFormat.TRANSLUCENT;
	     params.type = WindowManager.LayoutParams.TYPE_TOAST;
	     params.setTitle("Toast");
	     params.gravity = Gravity.LEFT | Gravity.TOP;
        //设置控件布局的参数
	     
	     params.x = sp.getInt("lastx", 0);
	     params.y = sp.getInt("lasty", 0);

        //获得布局文件的实例
		view = View.inflate(getApplicationContext(), R.layout.show_location, null);
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_location);
		
		int background = sp.getInt("background", 0);
		if(background == 0){
			ll.setBackgroundResource(R.drawable.call_locate_gray); //���ñ���
		}else if(background == 1){
			ll.setBackgroundResource(R.drawable.call_locate_orange);
		}else{
			ll.setBackgroundResource(R.drawable.call_locate_green);
		}
				
		TextView tv = (TextView) view.findViewById(R.id.location_tv);
		tv.setTextSize(this.getResources().getDimension(R.dimen.atool_textsize_noservice));//���������С
		tv.setText(address);
		AddressServiec.this.windowManager.addView(view,params);
	}


}
