package com.example.androiddefprot.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 *
 * @Title: TaskUitl.java
 * @Package com.example.androiddefprot.util
 * @Description: 进程工具类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TaskUitl {

    //杀死所有进程，除了系统默认进程
    public static void killAllProcess(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infos =  am.getRunningAppProcesses();
        for(RunningAppProcessInfo info : infos){
            String packageName = info.processName;
            am.killBackgroundProcesses(packageName);
        }
    }


    //得到进程的数量
    public static int getProcessCount(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        return list.size();
    }

    //得到可用的内存数
    public static String getMemeorySize(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        return TextFormater.getDataSize(outInfo.availMem);
    }


}
