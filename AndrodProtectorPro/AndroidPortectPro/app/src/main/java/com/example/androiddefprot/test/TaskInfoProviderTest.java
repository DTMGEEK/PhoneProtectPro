package com.example.androiddefprot.test;

import java.util.List;

import com.example.androiddefprot.domain.TaskInfo;
import com.example.androiddefprot.engine.TaskInfoProvider;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.test.AndroidTestCase;

/*
 * 测试
 */
public class TaskInfoProviderTest extends AndroidTestCase {
	
	
	public void getAllTask() throws Exception{
		TaskInfoProvider a = new TaskInfoProvider(this.getContext());
		ActivityManager am = (ActivityManager) this.getContext().getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		List<TaskInfo> taskinfo = a.getAllTasks(list);
		System.out.println(taskinfo.size());
		for(TaskInfo info:taskinfo){
			System.out.println(info.getAppname());
		}
	}

}
