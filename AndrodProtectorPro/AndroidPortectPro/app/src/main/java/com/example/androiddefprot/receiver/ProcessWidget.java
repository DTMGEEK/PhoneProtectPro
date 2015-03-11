package com.example.androiddefprot.receiver;

import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.service.UpdateWidgetService;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *
 * @Title: ProcessWidget.java
 * @Package com.example.androiddefprot.receiver
 * @Description:  *
 * 根据配置文件 每隔固定的时间 更新一下界面
 * 最小值 半个小时 1800000毫秒
 * onRecevie - > onUpdate
 *
 * 注意 widget这个组件不是现实在我们的应用程序里面
 * 显示在桌面的应用程序
 * 不同的桌面 他们的widget的创建和销毁对应的 回调的事件可能会有不能
 * android luncher / htc sence / 米ui / 360桌面/awt /qq桌面/....
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class ProcessWidget extends AppWidgetProvider {

	private static final String TAG = "ProcessWidget";
	private Intent intent = null;

    // widget第一次创建的时候 执行的方法
    // 初始化widget数据的操作,开启以后后台
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		LogManagement.i(TAG, "widget onEnabled");
		intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
	}



    //当某一个widget被删除的时候 会执行ondelete方法
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		LogManagement.i(TAG, "widget onDeleted");
		intent = new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}


}
