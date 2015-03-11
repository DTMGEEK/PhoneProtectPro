package com.example.androiddefprot.domain;


/**
 *
 * @Title: CacheInfo.java
 * @Package com.example.androiddefprot.domain
 * @Description: 缓存信息的（实体）类
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class CacheInfo {
	
	private String name;//app的名称
	
	private String packagename;//包名
	
	private String code_size;//app的大小
	
	private String cache_size;//缓存的大小
	
	private String data_size;//数据的大小




    public CacheInfo() {
		super();
		// TODO Auto-generated constructor stub
	}



	public CacheInfo(String name, String packagename, String code_size,
			String cache_size, String data_size) {
		super();
		this.name = name;
		this.packagename = packagename;
		this.code_size = code_size;
		this.cache_size = cache_size;
		this.data_size = data_size;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getPackagename() {
		return packagename;
	}



	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}



	public String getCode_size() {
		return code_size;
	}



	public void setCode_size(String code_size) {
		this.code_size = code_size;
	}



	public String getCache_size() {
		return cache_size;
	}



	public void setCache_size(String cache_size) {
		this.cache_size = cache_size;
	}



	public String getData_size() {
		return data_size;
	}



	public void setData_size(String data_size) {
		this.data_size = data_size;
	}
	
	
	
	

}
