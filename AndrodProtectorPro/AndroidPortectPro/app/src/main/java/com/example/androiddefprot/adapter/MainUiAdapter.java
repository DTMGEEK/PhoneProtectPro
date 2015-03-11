package com.example.androiddefprot.adapter;


import com.example.androiddefprot.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *
 * @Title: MainUiAdapter.java
 * @Package com.example.androiddefprot.adapter
 * @Description: 主界面填充布局的数据适配器
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class MainUiAdapter extends BaseAdapter {
	
	private Context context = null;
	private LayoutInflater inflater = null;  //布局填充器
	private static ImageView iv_mainscreen = null; //加入static是为了防止GridView多长加载 从而优化程序
	private static TextView tv_mainscreen = null;   //加入static是为了防止GridView多长加载 从而优化程序
	private SharedPreferences sp = null;
	
	
	
	public MainUiAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);  //实例化程序时候就初始化infalter
		this.sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

    //主屏幕功能列表名称
	private static int[] names = {
		    R.string.mobile_defender,R.string.mobile_guard,
			R.string.mobile_sofeware_manage, R.string.mobile_task_manage,
			R.string.mobile_flow_manage,R.string.mobile_killvirue,
			R.string.mobile_senior_tools,R.string.mobile_system_optimization,
			R.string.mobile_setting_center, R.string.mobile_aboutsoftware 
			
	};

    //主屏幕功能列表 图标
	private static int[] icons = { R.drawable.widget05, R.drawable.widget02,
			R.drawable.widget01, R.drawable.widget07, R.drawable.widget05,
			R.drawable.widget04, R.drawable.widget03, R.drawable.widget06,
			R.drawable.widget08,R.drawable.widget09
	};

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		
		return position;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}


    /**
     * 适配器中每一项显示的视图
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.mainscreen_item, null);
		this.iv_mainscreen = (ImageView) view.findViewById(R.id.iv_mainscreen);
		this.tv_mainscreen = (TextView) view.findViewById(R.id.tv_mainscreen);
		this.iv_mainscreen.setImageResource(this.icons[position]);
		this.tv_mainscreen.setText(this.names[position]);
		if(position == 0){
			String name = this.sp.getString("lostname", null);
			if(name != null){
				tv_mainscreen.setText(name);
			}
		}
		return view;
	}

}
