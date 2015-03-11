package com.example.androiddefprot;

import com.example.androiddefprot.domain.TaskInfo;
import com.example.androiddefprot.receiver.LockScreenReceiver;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *
 * @Title: MyApplication.java
 * @Package com.example.androiddefprot
 * @Description: 存储应用程序中的全局变量，可以提供个各个组件使用（activity，service，contentprovider，broadcastreceiver）
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class MyApplication extends Application {

    public TaskInfo taskinfo;

    @Override
    public void onCreate() {
        super.onCreate();

        //手工注册一个广播接收器
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF); //这个广播接收器只是关注锁屏事件
        filter.setPriority(1000);//设置优先级别
        LockScreenReceiver receiver = new LockScreenReceiver();
        this.registerReceiver(receiver, filter);
    }

}
