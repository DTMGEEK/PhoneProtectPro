package com.example.androiddefprot.engine;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.ContactInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;



/**
 *
 * @Title: ContactInfoService.java
 * @Package com.example.androiddefprot.engine
 * @Description: 通过ContentResolver 查询出系统的联系人信息。
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class ContactInfoService{
	
	private Context context;

	
	public ContactInfoService() {
		super();
	}


	public ContactInfoService(Context context) {
		super();
		this.context = context;
	}


    //1.获取联系人的id
    //2.根据联系人的id 获取联系人名字
    //3.根据联系人的id 数据的type 获取到对应的数据(电话,email);
	public  List<ContactInfo> getContactInfos(){
		Cursor cursor = null;
		ContactInfo contactinfo = null;
		ContentResolver resolver = this.context.getContentResolver();
		List<ContactInfo> infos = new ArrayList<ContactInfo>();           //定义一个List用于保存，ContactInfo的信息。
		Uri uri = Uri.parse(context.getString(R.string.ContactInfo_uri_raw_contacts));
		Uri datauri = Uri.parse(context.getString(R.string.ContactInfo_uri_data));
		cursor = resolver.query(uri, null, null, null, null);
		while(cursor.moveToNext()){
			contactinfo = new ContactInfo();
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("display_name"));
			contactinfo.setName(name);
			Cursor datacursor = resolver.query(datauri, null, "raw_contact_id=?", new String[]{id}, null);
            //以上查询出查询出了联系人的姓名，以及用于下面查询的id。
			 while (datacursor.moveToNext()) {
					//mimetype
					String type = datacursor.getString(datacursor.getColumnIndex("mimetype"));
					if("vnd.android.cursor.item/phone_v2".equals(type)){
						String number = datacursor.getString(datacursor.getColumnIndex("data1"));
						contactinfo.setPhone(number);
					}
			    }
			datacursor.close();
			infos.add(contactinfo);
			contactinfo = null;
		}
		return infos;
	}

}
