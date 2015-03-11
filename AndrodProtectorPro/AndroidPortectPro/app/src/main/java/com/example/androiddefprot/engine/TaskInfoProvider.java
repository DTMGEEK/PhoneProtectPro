package com.example.androiddefprot.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.TaskInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;


/**
 *
 * @Title: TaskInfoProvider.java
 * @Package com.example.androiddefprot.engine
 * @Description:进程信息提供类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TaskInfoProvider {
	
	private Context context;
	private PackageManager pm = null;
	private ActivityManager am = null;
	
	
	public TaskInfoProvider(Context context) {
		super();
		this.context = context;
		this.pm = context.getPackageManager();
		this.am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

    /**
     * 获得所有进程信息
     * @param appProcessInfos
     * @return
     */
	public List<TaskInfo> getAllTasks(List<RunningAppProcessInfo> appProcessInfos){
	   List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
	   for(RunningAppProcessInfo info:appProcessInfos){
		   TaskInfo taskInfo = null;
			   try {
				taskInfo = new TaskInfo();
				   taskInfo.setPid(info.pid);
				   String packageName = info.processName;
				   taskInfo.setPackagename(packageName);
				   
				   ApplicationInfo appInfo = this.pm.getPackageInfo(packageName, 0).applicationInfo;
				   
				   taskInfo.setAppicon(appInfo.loadIcon(pm));
				   taskInfo.setAppname(appInfo.loadLabel(pm).toString().trim());
				   if(filterApp(appInfo)){
					   taskInfo.setSystemapp(false);
				   }else{
					   taskInfo.setSystemapp(true);
				   }
				   
				   taskInfo.setAppname(appInfo.loadLabel(pm).toString().trim());

                   //获得应用占用的内存
				   MemoryInfo[] memoryInfos = this.am.getProcessMemoryInfo(new int[]{info.pid});
				   int memorysize = memoryInfos[0].getTotalPrivateDirty();
				   taskInfo.setMemorysize(memorysize);
				   
				   taskInfos.add(taskInfo);
				   taskInfo = null;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo = new TaskInfo();
				taskInfo.setPackagename(info.processName);
				taskInfo.setAppname(info.processName);
				Drawable icon = context.getResources().getDrawable(R.drawable.ic_launcher);
				taskInfo.setAppicon(icon);
				int pid = taskInfo.getPid();
				taskInfo.setPid(pid);
				taskInfo.setSystemapp(true);
				MemoryInfo[] memoryInfo = this.am.getProcessMemoryInfo(new int[]{pid});
				int memorysize = memoryInfo[0].getTotalPrivateDirty();
				taskInfo.setMemorysize(memorysize);
				taskInfos.add(taskInfo);
				taskInfo = null;
			}
	   }
	   return taskInfos;
	}




    /**
     * 判断某个应用程序是 不是三方的应用程序
     * @param info
     * @return
     */
	public boolean filterApp(ApplicationInfo info){
		if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
			return true;
		}else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
			return true;
		}
		return false;
	}


}
