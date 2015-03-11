package com.example.androiddefprot.domain;

import android.graphics.drawable.Drawable;

/**
 *
 * @Title: AppInfo.java
 * @Package com.example.androiddefprot.domain
 * @Description: 应用信息的（实体）类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppInfo {

	private Drawable icon;   //应用图标
	private String appname;  //应用名称
	private String packname;  //包名
	private boolean isSystemApp; //是不是系统应用
	
	public Drawable getIcon() {
		return icon;
	}
	
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public String getPackname() {
		return packname;
	}
	
	public void setPackname(String packname) {
		this.packname = packname;
	}
	
	public boolean isSystemApp() {
		return isSystemApp;
	}
	
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}
	
	
}
