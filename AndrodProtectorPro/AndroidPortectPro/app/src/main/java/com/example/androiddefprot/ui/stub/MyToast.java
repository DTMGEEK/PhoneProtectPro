package com.example.androiddefprot.ui.stub;

import com.example.androiddefprot.R;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * @Title: MyToast.java
 * @Package com.example.androiddefprot.ui.stub
 * @Description: 自定义Toast
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class MyToast {

	public static void showToast(Context context,int iconid,String text){
		View view = View.inflate(context, R.layout.my_toast, null);
		ImageView my_toast_iv = (ImageView) view.findViewById(R.id.my_toast_iv);
		TextView my_toast_tv = (TextView) view.findViewById(R.id.my_toast_tv);
		my_toast_iv.setImageResource(iconid);
		my_toast_tv.setText(text);
		Toast toast = new Toast(context);
		toast.setDuration(0);
		toast.setView(view);
		toast.show();
	}
	
}
