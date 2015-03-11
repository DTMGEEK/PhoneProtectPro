package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

/**
 *
 * @Title: TaskSettingActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 程序进程设置菜单
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TaskSettingActivity extends Activity {

    private TextView task_setting_tv_settingshow;
    private CheckBox task_setting_show_ck;
    private TextView task_setting_tv_clear;
    private CheckBox task_setting_clear_ck;
    private SharedPreferences sp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.task_setting);
        this.task_setting_tv_settingshow = (TextView) this.findViewById(R.id.task_setting_tv_settingshow);
        this.task_setting_show_ck = (CheckBox) this.findViewById(R.id.task_setting_show_ck);
        this.task_setting_tv_clear = (TextView) this.findViewById(R.id.task_setting_tv_clear);
        this.task_setting_clear_ck = (CheckBox) this.findViewById(R.id.task_setting_clear_ck);

        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);

        boolean showsystemapp = this.sp.getBoolean("showsystemapp", false);
        boolean killprocess = this.sp.getBoolean("killprocess", false);
        if(showsystemapp){
            task_setting_show_ck.setChecked(true);
            task_setting_tv_settingshow.setText("显示系统进程");
        }else{
            task_setting_show_ck.setChecked(false);
            task_setting_tv_settingshow.setText("不显示系统进程");
        }

        task_setting_show_ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = TaskSettingActivity.this.sp.edit();
                if(isChecked){
                    task_setting_tv_settingshow.setText("显示系统进程");
                    editor.putBoolean("showsystemapp", true);
                    editor.commit();
                    TaskSettingActivity.this.setResult(200);
                }else{
                    task_setting_tv_settingshow.setText("不显示系统进程");
                    editor.putBoolean("showsystemapp", false);
                    editor.commit();
                    TaskSettingActivity.this.setResult(200);
                }
            }
        });

        if(killprocess){
            task_setting_clear_ck.setChecked(true);
            task_setting_tv_clear.setText("锁屏自动清理内存");
        }else{
            task_setting_clear_ck.setChecked(false);
            task_setting_tv_clear.setText("锁屏不清理内存");
        }
        task_setting_clear_ck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = TaskSettingActivity.this.sp.edit();
                if(isChecked){
                    task_setting_tv_clear.setText("锁屏自动清理内存");
                    editor.putBoolean("killprocess", true);
                    editor.commit();

                }else{
                    task_setting_tv_clear.setText("锁屏不清理内存");
                    editor.putBoolean("killprocess", false);
                    editor.commit();
                }
            }
        });

    }

}
