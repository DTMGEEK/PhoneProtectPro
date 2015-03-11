package com.example.androiddefprot.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import android.app.ProgressDialog;

/**
 *
 * @Title: DownLoadFileTask.java
 * @Package com.example.androiddefprot.engine
 * @Description: 下载新的apk
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class DownLoadFileTask {

	public static File getFile(String path,String filepath,ProgressDialog pd) throws Exception{
		
		URL url = new URL(path);
		HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setReadTimeout(2000);
		InputStream is = conn.getInputStream();
		File file = new File(filepath);
		FileOutputStream fos = new FileOutputStream(file);
		int len = 0;
		byte[] buffer = new byte[1024];
		int process = 0;
		
		if(conn.getResponseCode() == 200){
			while((len = is.read(buffer))!= -1){
				fos.write(buffer,0,len);
				process += len;
				pd.setProgress(process);
			}

			return file;
		}
        fos.flush();
        fos.close();
        is.close();
		return null;
	}
	
	
}
