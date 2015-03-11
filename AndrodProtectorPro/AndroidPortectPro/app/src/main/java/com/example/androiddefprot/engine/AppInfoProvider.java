package com.example.androiddefprot.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


/**
 *
 * @Title: AppInfoProvider.java
 * @Package com.example.androiddefprot.engine
 * @Description: 系统应用信息提供类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppInfoProvider {

	private Context context;
	private PackageManager manager;
	
	
	public AppInfoProvider(Context context) {
		super();
		this.context = context;
		manager = context.getPackageManager();
	}


    /**
     * 得到所有安装了的软件信息
     * @return 应用程序的集合
     */
	public List<AppInfo> getAllApp(){
		
		List<AppInfo> appInfos = new ArrayList<AppInfo>();  //����appinfo��Ϣ
		List<PackageInfo> packInfos = this.manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES); //��ȡ���а�װ�˵������Ϣ
		for(PackageInfo info:packInfos){
			AppInfo appinfo = new AppInfo();
			String packName = info.packageName;
			
			appinfo.setPackname(packName);
			
			ApplicationInfo applicationinfo = info.applicationInfo;
			Drawable icon = applicationinfo.loadIcon(manager);		
			appinfo.setIcon(icon);
			
			appinfo.setAppname(applicationinfo.loadLabel(manager).toString());  //�������
			
			appinfo.setSystemApp(filterApp(applicationinfo));
			
			appInfos.add(appinfo);
		}
		return appInfos;
	}



    /**
     * 通过以上方法，可以得到手机中安装的所有应用程序，既包括了手动安装的apk包的信息，也包括了系统预装的应用软件的信息，要区分这两类软件可使用以下方法:
     *	a.从packageInfoList获取的packageInfo，再通过packageInfo.applicationInfo获取applicationInfo。
     *	b.判断(applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)的值，该值大于0时，表示获取的应用为系统预装的应用，反之则为手动安装的应用。
     * 判断是系统应用还是第三方应用
     * @param appinfo
     * @return
     */
	public boolean filterApp(ApplicationInfo appinfo){
		
		if((appinfo.flags & appinfo.FLAG_SYSTEM) <= 0){
			return false;
		}
		return true;
	}
	
	
}
