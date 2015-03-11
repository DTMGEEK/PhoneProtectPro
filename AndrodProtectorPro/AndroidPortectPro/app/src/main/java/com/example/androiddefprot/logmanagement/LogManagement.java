package com.example.androiddefprot.logmanagement;

import android.util.Log;

/**
 *
 * @Title: LogManagement.java
 * @Package com.example.androiddefprot.logmanagement
 * @Description: 自定义的log管理
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class LogManagement {

    //通过改变LOGLEVEL 来控制log是否输出
	private static final int LOGLEVEL = 6;
	private static final int VERBOS = 1; 
	private static final int DEBUG = 2; 
	private static final int INFOS = 3; 
	private static final int WARN = 4;
	private static final int ERROR = 5; 

	
	public static void v(String tag,String msg){
		if(LOGLEVEL > VERBOS){
			Log.v(tag, msg);
		}
	}
	
	
	public static void d(String tag,String msg){
		if(LOGLEVEL > DEBUG){
			Log.d(tag, msg);
		}
	}
	
	public static void i(String tag,String msg){
		if(LOGLEVEL > INFOS){
			Log.i(tag, msg);
		}
	}
	
	public static void w(String tag,String msg){
		if(LOGLEVEL > WARN){
			Log.w(tag, msg);
		}
	}
	
	public static void e(String tag,String msg){
		if(LOGLEVEL > ERROR){
			Log.e(tag, msg);
		}
	}
	
	
}
