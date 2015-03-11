package com.example.androiddefprot.domain;



/**
 *
 * @Title: SmsInfo.java
 * @Package com.example.androiddefprot.domain
 * @Description: 信息实体类，用来保存单个信息的数据
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SmsInfo {
	
	private String id;         
	private String address;  //收到短信的手机号码
	private String date;	   //收到短信的时间
	private int type;        //1.代表接受，2。代码发送
	private String body;	//信息的内容
	
	
	public SmsInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SmsInfo(String id, String address, String date, int type, String body) {
		super();
		this.id = id;
		this.address = address;
		this.date = date;
		this.type = type;
		this.body = body;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}
	

}
