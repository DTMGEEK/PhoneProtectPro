package com.example.androiddefprot.domain;



/**
 *
 * @Title: UpdateInfo.java
 * @Package com.example.androiddefprot.domain
 * @Description:app升级信息domain类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class UpdateInfo {

    //版本号
	private String version;
    //描述
	private String description;
    //url
	private String apkurl;
	
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getApkurl() {
		return apkurl;
	}
	
	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}

}
