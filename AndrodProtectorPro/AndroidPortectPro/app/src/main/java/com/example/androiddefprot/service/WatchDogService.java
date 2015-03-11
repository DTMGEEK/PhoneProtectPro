package com.example.androiddefprot.service;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.db.dao.AppLockDao;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.ui.LockScreenActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 *
 * @Title: WatchDogService.java
 * @Package com.example.androiddefprot.service
 * @Description: 看门狗服务，开启服务后，会观察系统中被锁定的应用，看是否需要启动程序锁
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class WatchDogService extends Service {

	protected static final String TAG = "WatchDogService";
	private List<String> lockApps = null;
	private AppLockDao dao = null;
	private boolean flag = true;
	private ActivityManager activityManager = null;
	private Intent lockScreenIntent = null;
	private List<String> tempstopApps = null;
	private MyBinder binder = null;
	private KeyguardManager keyguardManager = null;


    //为了让LockScreenActivity可以调用到IService里面的方法，通过IBinder调用。
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return binder;
	}


    //通过IBinder对象调用下面的方法
	public class MyBinder extends Binder implements IService{

		@Override
		public void callAppProtectStart(String packagename) {
			appProtectStart(packagename);
		}

		@Override
		public void callAppProtectStop(String packagename) {
			appProtectStop(packagename);
		}
	}


    /**
     * 把某个包移除临时停止保护的集合，重启开启保护
     * @param packagename
     */


    public void appProtectStart(String packagename){
		if(this.tempstopApps.contains(packagename)){
			tempstopApps.remove(packagename);
		}
	}

    /**
     * 包某个包名添加进入临时停止保护的集合，临时停止保护
     * @param packagename
     */
	public void appProtectStop(String packagename){
		this.tempstopApps.add(packagename);
	}
	
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.getContentResolver().registerContentObserver(
				Uri.parse("content://com.text.androiddefprot.provider"), true,
				new MyObserver(new Handler()));
        // 观察某个内容的信息是否改变

        //获取手机是否锁屏
		WatchDogService.this.keyguardManager = (KeyguardManager)WatchDogService.this.getSystemService(KEYGUARD_SERVICE);
		
		binder = new MyBinder();
		this.dao = new AppLockDao(WatchDogService.this);
		this.lockApps = this.dao.findAll();
		this.activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		lockScreenIntent = new Intent(WatchDogService.this,LockScreenActivity.class);
		tempstopApps = new ArrayList<String>();
		lockScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//服务不存在任务栈，在服务中启动activity必须加上此行代码
		new Thread(){
			@Override
			public void run() {
				super.run();
				
				while(WatchDogService.this.flag){
					try {
                        //判断是不是在锁屏状态，是就情况所以临时暂停保护的集合
						if(WatchDogService.this.keyguardManager.inKeyguardRestrictedInputMode()){
							//�����ʱ�ļ���
							tempstopApps.clear();
						}

                        //lockapps 信息为最新的
                        //lockapps = dao.getAllApps();
                        // 得到当前正在运行程序的包名
                        // 返回系统里面的任务栈的信息 , taskinfos的集合里面只有一个元素
                        // 内容就是当前正在运行的进程对应的任务栈
						
						
						List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
						RunningTaskInfo currenttask = tasks.get(0);
						String packagename = currenttask.topActivity.getPackageName();
						LogManagement.i(TAG, "当前运行的程序" + packagename);
						if(lockApps.contains(packagename)){

                            //如果当前应用程序需要，临时停止保护
							if(WatchDogService.this.tempstopApps.contains(packagename)){
								this.sleep(1000);     //如果要暂停保护就让线程先睡眠
                                continue;
							}
							LogManagement.i(TAG, "需要锁定");
							lockScreenIntent.putExtra("packagename", packagename); 
							WatchDogService.this.startActivity(lockScreenIntent);
						}else{
							//todo����
						}
						
						sleep(1000);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}



    /**
     * 服务被停职的时候，同时停止线程
     */
	@Override
	public void onDestroy() {
		
		super.onDestroy();
		WatchDogService.this.flag = false;
	}



    /**
     * 自定义观察者
     * @author jake
     *
     */
	private class MyObserver extends ContentObserver{

		public MyObserver(Handler handler) {
			super(handler);
		}
		
		//����ݷ���ı��������������
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			//���¸���lockApps�������������
			LogManagement.i(TAG, "------------------------------lockApps内容发生了改变");
			WatchDogService.this.lockApps = WatchDogService.this.dao.findAll();//重新获得锁定的应用程序
    }
	}
	
	
}
