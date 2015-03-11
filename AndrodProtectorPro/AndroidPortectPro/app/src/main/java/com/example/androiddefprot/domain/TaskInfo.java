package com.example.androiddefprot.domain;

import android.graphics.drawable.Drawable;

/**
 *
 * @Title: TaskInfo.java
 * @Package com.example.androiddefprot.domain
 * @Description: 线程信息Domain类（实体）
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TaskInfo {

	private String appname;  //app名字
	private int pid;          //线程id
 	private Drawable appicon; //app图标
	private String packagename;  //包名
	private boolean isCheck;   //是否被选中
	private int memorysize;     //占用内存大小
	private boolean isSystemapp; //是不是系统预装应用
	
	
	public String getAppname() {
		return appname;
	}
	
	public void setAppname(String appname) {
		this.appname = appname;
	}
	
	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public Drawable getAppicon() {
		return appicon;
	}
	
	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
	
	public String getPackagename() {
		return packagename;
	}
	
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	
	public boolean isCheck() {
		return isCheck;
	}
	
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	
	public int getMemorysize() {
		return memorysize;
	}
	
	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}

	public boolean isSystemapp() {
		return isSystemapp;
	}

	public void setSystemapp(boolean isSystemapp) {
		this.isSystemapp = isSystemapp;
	}
	
	
	

}
