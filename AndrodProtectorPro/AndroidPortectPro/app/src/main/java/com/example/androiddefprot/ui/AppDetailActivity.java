package com.example.androiddefprot.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.example.androiddefprot.MyApplication;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.TaskInfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 *
 * @Title: AppDetailActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 显示该程序的包名和软件名以及所需要的权限的activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppDetailActivity extends Activity {
	
	private TextView app_detail_tv_appname;
	private TextView app_detial_tv_packagename;
	private ScrollView app_detail_sv = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.app_detail);
		this.app_detail_tv_appname = (TextView) this.findViewById(R.id.app_detail_tv_appname);
		this.app_detial_tv_packagename = (TextView) this.findViewById(R.id.app_detial_tv_packagename);
		this.app_detail_sv = (ScrollView) this.findViewById(R.id.app_detail_sv);
		
		MyApplication app = (MyApplication) this.getApplication();
		TaskInfo taskinfo = app.taskinfo;
		app_detail_tv_appname.setText(taskinfo.getAppname());
		app_detial_tv_packagename.setText(taskinfo.getPackagename());

        //应用反射获得android.widget.AppSecurityPermissions类，在得到getPermissionsView中View的返回值
		try {
			
			Class clazz = getClass().getClassLoader().loadClass("android.widget.AppSecurityPermissions");  
	        Constructor constructor = clazz.getConstructor(new Class[]{Context.class,String.class});  
	        Object object = constructor.newInstance(new Object[]{this,taskinfo.getPackagename()});  
	        Method method = clazz.getDeclaredMethod("getPermissionsView", new Class[]{});  
	        View view = (View) method.invoke(object, new Object[]{});  
	        app_detail_sv.addView(view);
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	

}
