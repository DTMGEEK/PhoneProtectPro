package com.example.androiddefprot.engine;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpConnection;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Message;

import com.example.androiddefprot.domain.UpdateInfo;
import com.example.androiddefprot.ui.SplashActivity;


/**
 *
 * @Title: UpdateInfoService.java
 * @Package com.example.androiddefprot.engine
 * @Description: app软件升级服务类，获得服务器上的信息
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class UpdateInfoService {

	private Context context;
	
	public UpdateInfoService(Context context) {
		this.context = context;
	}



    /**
     *
     * @param urlid  服务器对应的string路径的id
     * @return  更新信息
     */
	public UpdateInfo getUpdateInfo(final int urlid) throws Exception{
		String path = context.getResources().getString(urlid);
		URL url = new URL(path);
		HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(2000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return  UpdateInfoParser.getUpdateInfo(is);
	}
	
	
}
