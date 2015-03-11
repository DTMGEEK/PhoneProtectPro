package com.example.androiddefprot.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.example.androiddefprot.domain.SmsInfo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;



/**
 *
 * @Title: SmsInfoService.java
 * @Package com.example.androiddefprot.engine
 * @Description:短息备份读取短信息类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SmsInfoService {
	
	private Context context;

	public SmsInfoService(Context context){
		this.context = context;
	}
	
	
	public List<SmsInfo> getSmsInfos(){
		List<SmsInfo> infos = new ArrayList<SmsInfo>();//保存短信的信息
		ContentResolver resolver = context.getContentResolver(); 
		Uri uri = Uri.parse("content://sms");
		Cursor cursor = resolver.query(uri, new String[]{"_id","address","date","type","body"}, null, null, "date desc");
		SmsInfo smsInfo = null;
		while(cursor.moveToNext()){
			String id = cursor.getString(0);
			String address = cursor.getString(1);
			String date = cursor.getString(2);
			int type = cursor.getInt(3);
			String body = cursor.getString(4);
			smsInfo = new SmsInfo(id,address,date,type,body);
			infos.add(smsInfo);
			smsInfo = null;
		}
		cursor.close();
		return infos;
	}



    /**
     * 短信还原操作类，读取xml保存的文件后，还原到系统的短信中
     * @param path
     * @param pd
     * @throws Exception
     */
	public void restoreSms(String path,ProgressDialog pd) throws Exception{
		File file = new File(path);
		ContentValues values = null;
		FileInputStream fis = new FileInputStream(file);  
		XmlPullParser parser = Xml.newPullParser(); //xml������
		parser.setInput(fis, "UTF-8");              
		int type = parser.getEventType();
		int currentcount = 0;
		while(type != parser.END_DOCUMENT){
			switch(type){
			
				 case XmlPullParser.START_TAG:
					 if("count".equals(parser.getName())){   
						 String count = parser.nextText();     //�õ����ŵ�������
						 pd.setMax(Integer.parseInt(count));   //����ProgressDialog�����ֵ
					 }
					 if("sms".equals(parser.getName())){
						 values = new ContentValues(); 
						 
					 }else if("address".equals(parser.getName())){
						 values.put("address", parser.nextText());
						 
					 }else if("date".equals(parser.getName())){
						values.put("date", parser.nextText());
						 
					 }else if("type".equals(parser.getName())){
						values.put("type", parser.nextText());
						 
					 }else if("body".equals(parser.getName())){
						 values.put("body", parser.nextText());
					 }
				break;
				
				
				case XmlPullParser.END_TAG:
					if("sms".equals(parser.getName())){
						ContentResolver resolver = context.getContentResolver();
						Uri sms_uri = Uri.parse("content://sms/");
						resolver.insert(sms_uri, values);
						values = null;
						currentcount++;
						pd.setProgress(currentcount);
					}
				break;
			}
			type = parser.next();
		}
		
	}
	
	
	
	
	
	
	
}
