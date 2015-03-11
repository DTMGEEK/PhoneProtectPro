package com.example.androiddefprot.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.util.MD5Encoder;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


/**
 *
 * @Title: AntiVirutsActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 杀毒Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AntiVirutsActivity extends Activity {
	
	private ImageView antivirus_iv = null;
	private ProgressBar antivirus_pb = null;
	private ScrollView antivirus_sv = null;
	private LinearLayout antivirus_ll = null;
	private SQLiteDatabase db = null;
	private AnimationDrawable anim = null;
	private static final int STOP = 1000;
	
	private String antivirus_db_name = null;
	private String antivirus_db_path = null;
	private boolean flagscanning = false;
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == STOP){
				anim.stop();
				antivirus_ll.removeAllViews();
			}
			TextView tv = new TextView(AntiVirutsActivity.this);
			AntiVirutsActivity.this.antivirus_pb.setVisibility(View.INVISIBLE);
			tv.setText(msg.obj.toString());
			antivirus_ll.setOrientation(LinearLayout.VERTICAL);
			antivirus_ll.addView(tv);
			antivirus_sv.scrollBy(0, 20);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.antivirus);
		this.antivirus_iv = (ImageView) this.findViewById(R.id.antivirus_iv);
		this.antivirus_pb = (ProgressBar) this.findViewById(R.id.antivirus_pb); 
		this.antivirus_sv = (ScrollView) this.findViewById(R.id.antivirus_sv);
		this.antivirus_ll = (LinearLayout) this.findViewById(R.id.antivirus_ll);
		this.antivirus_iv.setBackgroundResource(R.drawable.anti_anim);
		this.anim =  (AnimationDrawable) antivirus_iv.getBackground();
		
		this.antivirus_db_name = this.getResources().getString(R.string.antivirus_db_name);
		this.antivirus_db_path = this.getResources().getString(R.string.antivirus_db_path);
		copyFile();
		
		this.db = SQLiteDatabase.openDatabase(this.antivirus_db_path, null, SQLiteDatabase.OPEN_READONLY);
		
	}
	
	
	/**
	 * 
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(flagscanning){
			return false;
		}

		if(event.getAction() == MotionEvent.ACTION_UP){
			AntiVirutsActivity.this.antivirus_pb.setVisibility(View.VISIBLE);
			flagscanning = true;
			this.anim.start();
			new Thread(){
				@Override
				public void run() {
					super.run();
					
					int total = 0;
					int virustotal = 0;
					PackageManager pm = AntiVirutsActivity.this.getPackageManager();
					List<PackageInfo> infos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
							                         |PackageManager.GET_SIGNATURES);
					
					antivirus_pb.setMax(infos.size());
					for(PackageInfo info : infos){
						total++;
						Message msg = Message.obtain();
                        // 获取每一个应用程序的签名 获取到这个签名后 需要在数据库里面查询
						Signature[] signatures = info.signatures;
						String str = signatures[0].toCharsString();
						String md5 = MD5Encoder.encode(str);
						Cursor cursor = AntiVirutsActivity.this.db.rawQuery(
								"select desc from datable where md5 = ?",
								new String[] { md5 });
						if(cursor.moveToNext()){
							String desc = cursor.getString(0);
						
							msg.obj = info.packageName + ":" + desc;
							handler.sendMessage(msg);
							virustotal++;
						}
						msg.obj = info.packageName;
						handler.sendMessage(msg);
						cursor.close();
						antivirus_pb.setProgress(total);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Message msg = Message.obtain();
					msg.obj = "扫描完毕 ,共发现" + virustotal + "个病毒";
					handler.sendMessage(msg);
					flagscanning = false;
					msg.what = STOP;
					antivirus_pb.setProgress(0);
				}
			}.start();
		}
		return super.onTouchEvent(event);
	}



    //当数据库不存在的时候，把apk中的数据库文件拷贝到内存卡中
	private void copyFile() {
		AssetManager am = this.getAssets();
		try {
			File antivirusd_db = new File(antivirus_db_path);
			if(!antivirusd_db.exists()){
				InputStream is = am.open(antivirus_db_name);
				FileOutputStream fos = new FileOutputStream(antivirusd_db);
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = is.read(buffer)) != -1){
					fos.write(buffer);
				}
				fos.flush();
				fos.close();
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



    //当Activity销毁时，同时关闭数据库
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(this.db.isOpen()){
			this.db.close();
		}
	}
	

}
