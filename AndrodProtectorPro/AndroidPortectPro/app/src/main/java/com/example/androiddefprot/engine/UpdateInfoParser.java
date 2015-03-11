package com.example.androiddefprot.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.example.androiddefprot.domain.UpdateInfo;

/**
 * 
* @Title: UpdateInfoParser.java 
* @Package com.example.androiddefprot.engine 
* @Description: xml��Ϣ������ 
* @author lian_weijian@163.com   
* @version V1.0
 */
public class UpdateInfoParser {

    /**
     *
     * @Title: UpdateInfoParser.java
     * @Package com.example.androiddefprot.engine
     * @Description: xml信息解析类
     * @author lian_weijian@163.com
     * @version V1.0
     */
	public static UpdateInfo getUpdateInfo(InputStream is) throws Exception{
		
			XmlPullParser parser = Xml.newPullParser();
			UpdateInfo updateInfo = new UpdateInfo();
			parser.setInput(is, "utf-8");
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
					case XmlPullParser.START_TAG:
						if("version".equals(parser.getName())){
							String version = parser.nextText();
							updateInfo.setVersion(version);
						}else if("description".equals(parser.getName())){
							String description = parser.nextText();
							updateInfo.setDescription(description);
						}else if("apkurl".equals(parser.getName())){
							String apkurl = parser.nextText();
							updateInfo.setApkurl(apkurl);
						}
					break;
				}
				eventType = parser.next();
			}
		return updateInfo;
	}
	
}
