package com.example.androiddefprot.ui;

import java.util.List;






import com.example.androiddefprot.R;
import com.example.androiddefprot.db.dao.AppLockDao;
import com.example.androiddefprot.domain.AppInfo;
import com.example.androiddefprot.engine.AppInfoProvider;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @Title: AppLockActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 程序锁Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppLockActivity extends ActionBarActivity implements OnDismissCallback {
	
	private AppInfoProvider provider = null; //所以app信息提供者
	private List<AppInfo> AppInfos = null;  //保存所有app的信息
	private AppLockerAdapter adapter = null; 
	private AppLockDao dao = null;           //app数据库操作类
	private ListView lv = null;				 
	private LinearLayout ll_app_manager_loading = null;
	private List<String> lockAppInfos = null;     //保存被锁定的app信息
	private ImageView lock_app_iv = null;
	private View view = null;


    //启动一个新线程获取app的信息
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			adapter = new AppLockerAdapter();
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                    new SwipeDismissAdapter(adapter, AppLockActivity.this));

            swingBottomInAnimationAdapter.setAbsListView(lv);

            lv.setAdapter(swingBottomInAnimationAdapter);
            AppLockActivity.this.ll_app_manager_loading.setVisibility(View.INVISIBLE);
			//AppLockActivity.this.lv.setAdapter(adapter);
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
        //cardlist
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		this.setContentView(R.layout.app_lock);
		this.dao = new AppLockDao(AppLockActivity.this);
		this.lv = (ListView) this.findViewById(R.id.app_lock_lv);
		this.ll_app_manager_loading = (LinearLayout) this.findViewById(R.id.ll_app_manager_loading);

        //初始化UI
		this.initUI();
        //得到所有的app相信
		lockAppInfos = this.dao.findAll();

        //设置ListView的每一个项的监听
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                //设置动画
				TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f,
															   Animation.RELATIVE_TO_SELF,0.5f,
															   Animation.RELATIVE_TO_SELF,0.0f, 
															   Animation.RELATIVE_TO_SELF,0.5f);
				ta.setDuration(200);
				view.setAnimation(ta);
				ta.start();
                //得到包名
				String packagename = AppInfos.get(position).getPackname();
				ImageView iv = (ImageView) view.findViewById(R.id.lock_app_iv);
				if(AppLockActivity.this.dao.find(packagename)){
					//AppLockActivity.this.dao.delete(packagename);
					
					AppLockActivity.this
							.getContentResolver()
							.delete(Uri
									.parse("content://com.text.androiddefprot.provider/delete"),
									null, new String[] { packagename });
					lockAppInfos.remove(packagename);
					iv.setImageResource(R.drawable.unlock);
				}else{
					//AppLockActivity.this.dao.add(packagename);
					ContentValues values = new ContentValues();
					values.put("packagename", packagename);
					AppLockActivity.this
							.getContentResolver()
							.insert(Uri
									.parse("content://com.text.androiddefprot.provider/insert"),
									values);
					iv.setImageResource(R.drawable.lock);
				}
			}
		});
	}


    /**
     * 初始化UI
     */
	private void initUI(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				AppLockActivity.this.ll_app_manager_loading.setVisibility(View.VISIBLE);
				AppInfos = new AppInfoProvider(AppLockActivity.this).getAllApp();
				AppLockActivity.this.handler.sendEmptyMessage(0);
			}
		}.start();
	}



    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

    }


    /**
     * 程序锁数据适配器
     * @author jake
     *
     */
	private class AppLockerAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return AppInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return AppInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView == null){
				view = View.inflate(AppLockActivity.this, R.layout.lock_app_item, null);
			}else{
				view = convertView;
			}
			AppInfo appinfo = AppInfos.get(position);
			ImageView lock_app_icon = (ImageView) view.findViewById(R.id.lock_app_icon);
			TextView lock_app_tv_name = (TextView) view.findViewById(R.id.lock_app_tv_name);
			TextView lock_app_tv_packname = (TextView) view.findViewById(R.id.lock_app_tv_packname);
			ImageView lock_app_iv = (ImageView) view.findViewById(R.id.lock_app_iv);
			
			lock_app_tv_packname.setText(appinfo.getPackname());
			lock_app_icon.setImageDrawable(appinfo.getIcon());
			lock_app_tv_name.setText(appinfo.getAppname());
			if(dao.find(appinfo.getPackname())){
				lock_app_iv.setImageResource(R.drawable.lock);
			}else{
				lock_app_iv.setImageResource(R.drawable.unlock);
			}
			return view;
		}
		
	}
	
	
	
}
