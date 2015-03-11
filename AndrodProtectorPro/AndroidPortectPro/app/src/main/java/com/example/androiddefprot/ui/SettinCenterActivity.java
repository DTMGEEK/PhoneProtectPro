package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;
import com.example.androiddefprot.service.WatchDogService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 *
 * @Title: SettinCenterActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 设置中心Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SettinCenterActivity extends Activity {

    private CheckBox setting_applock_cb = null;
    private TextView setting_applock_tv = null;
    private Intent watchdogIntent = null;
    private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setting_center);
        setting_applock_cb = (CheckBox) this.findViewById(R.id.setting_applock_cb);
        setting_applock_tv = (TextView) this.findViewById(R.id.setting_applock_tv);

        //在SharedPreferences保存用户是否启动了程序锁
        this.sp = SettinCenterActivity.this.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean islockscreenservice = sp.getBoolean("islockscreenservice", false);

        //初始化
        if(islockscreenservice){
            this.setting_applock_cb.setChecked(true);
            String str = SettinCenterActivity.this.getResources().getString(R.string.setting_center_tv_text);
            setting_applock_tv.setText(str);
        }

        //根据CheckBox来判断是否启动watchdog
        watchdogIntent = new Intent(SettinCenterActivity.this,WatchDogService.class);
        setting_applock_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SettinCenterActivity.this.startService(watchdogIntent);
                    String str = SettinCenterActivity.this.getResources().getString(R.string.setting_center_tv_text);
                    setting_applock_tv.setText(str);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("islockscreenservice", true);
                    editor.commit();
                }else{
                    SettinCenterActivity.this.stopService(watchdogIntent);
                    String str = SettinCenterActivity.this.getResources().getString(R.string.setting_center_tv_text_unservice);
                    setting_applock_tv.setText(str);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("islockscreenservice", false);
                    editor.commit();
                }
            }
        });
    }

}
