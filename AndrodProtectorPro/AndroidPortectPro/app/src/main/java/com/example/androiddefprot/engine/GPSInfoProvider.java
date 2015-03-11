package com.example.androiddefprot.engine;

import java.security.Provider;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;



/**
 *
 * @Title: GPSInfoProvider.java
 * @Package com.example.androiddefprot.engine
 * @Description: GPS信息提供类 本类使用单例设计，可以防止GPS被多次实例化，导致手机死机
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class GPSInfoProvider {

	private LocationManager manager = null;         
	private static GPSInfoProvider gpsProvider = null;
	private static SharedPreferences sp = null;
	private static Context context = null;
	private static MyLocationListener listener = null;



    //构造方法私有化
	private GPSInfoProvider() {
		super();
	}


    /**
     * 使用synchronized 使线程同步，只能获得一个实例。
     * @param context    上下文
     * @return   GPSInfoProvider的实例。
     */
	public static synchronized GPSInfoProvider getInstance(Context context){
		if(gpsProvider == null){
			gpsProvider = new GPSInfoProvider();
			GPSInfoProvider.context = context;
		}
		return gpsProvider;
	}


    /**
     *
     * @return 获得当前手机的位置信息
     */
	public String getLocation(){
		String location = null;
		this.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String provider = getProvider(manager);
		manager.requestLocationUpdates(provider, 6000, 50, getListener());
		this.sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		location = sp.getString("location", "");
		return location;
	}



    /**
     * 得到GPSProvider
     * @param manager   位置管理服务的实例
     * @return      返回最好的位置提供者
     */
	private String getProvider(LocationManager manager) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(true);
		criteria.setSpeedRequired(true);
		return manager.getBestProvider(criteria, true);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	private LocationListener getListener() {
		if(listener == null){
			listener = new MyLocationListener();
		}
		return listener;
	}


    /**
     * 停止获取GPS信息
     */
	public void stopProvider(){
		this.manager.removeUpdates(getListener());
	}



    /**
     * 获得GPS的经纬度，并且保存到SharedPreferences的xml文件中。
     * @author jake
     *
     */
	private class MyLocationListener implements LocationListener{

        /**
         * 当手机位置发生改变时候，调用该方法。
         */
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude" + location.getLatitude();
			String longitude = "longitude" + location.getLongitude();
			SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("location", latitude + "-" + longitude);
			editor.commit();
		}


        /**
         * 当手机状态发生改变的时候，调用，可用->不可用，不可用->可用。
         */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

        /**
         * 某一设备被打开
         */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
        /**
         * 某一设备被禁用
         */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}

	
	
}
