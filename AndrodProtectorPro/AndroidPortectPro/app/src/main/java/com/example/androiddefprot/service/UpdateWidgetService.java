package com.example.androiddefprot.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.androiddefprot.R;
import com.example.androiddefprot.receiver.LockScreenReceiver;
import com.example.androiddefprot.util.TaskUitl;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

/**
 *
 * @Title: UpdateWidgetService.java
 * @Package com.example.androiddefprot.service
 * @Description: 自动更新widget信息的后台服务类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class UpdateWidgetService extends Service {

	private Timer timer = null; //计时器
	private TimerTask timertask = null; //计时器任务管理类
	private AppWidgetManager manager = null; //widget管理类
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
        //初始化
		this.timer = new Timer();
		this.manager = AppWidgetManager.getInstance(getApplicationContext());//���widgetmanager
        //计时器任务类，每一次调用都启动一个新的线程
		timertask = new TimerTask() {
			
			@Override
			public void run() {
				ComponentName name = new ComponentName(
						"com.example.androiddefprot",
						"com.example.androiddefprot.receiver.ProcessWidget"); // 第一个参数是包名，第二个参数是类名，类一定要在包里面
				RemoteViews view = new RemoteViews("com.example.androiddefprot", R.layout.process_widget);    
				view.setTextViewText(
						R.id.process_widget_tv_process_count,
						"进程数目："
								+ TaskUitl
										.getProcessCount(getApplicationContext()));
				
				view.setTextColor(R.id.process_widget_tv_process_count, Color.RED);
				
				view.setTextViewText(
						R.id.process_widget_tv_process_memory,
						"可用内存"
								+ TaskUitl
										.getMemeorySize(getApplicationContext()));
				
				view.setTextColor(R.id.process_widget_tv_process_memory,Color.RED);
				Intent intent = new Intent(UpdateWidgetService.this,LockScreenReceiver.class);
				PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
				view .setOnClickPendingIntent(R.id.process_widget_btn_clear, pi);
				UpdateWidgetService.this.manager.updateAppWidget(name, view);  //更新widgetui组件
			}
		};
        //每隔一秒钟执行，每隔两秒更新界面
		timer.schedule(timertask, 1000, 2000);
	}
	
	
	
	@Override
	public void onDestroy() {
		this.timer.cancel();
		this.timer = null;
		this.manager = null;
		super.onDestroy();
	}
	
	
}
