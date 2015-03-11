package com.example.androiddefprot.ui;


import java.io.File;

import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.DownLoadFileTask;
import com.example.androiddefprot.engine.SmsInfoService;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.service.AddressServiec;
import com.example.androiddefprot.service.BackupSmsService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: AToolsActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 高级工具Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */

public class AToolsActivity extends Activity implements OnClickListener {

    private static final String TAG = "AToolsActivity";
    private static final int  SUCCESS = 1;
    private static final int  FAIL = 0;
    private TextView tv_atool_query = null;
    private TextView tv_atools_address = null;
    private CheckBox atools_cb_address = null;
    private TextView atools_tv_select_bg = null;
    private TextView atools_tv_change_location = null;
    private Intent serviceIntent = null;
    private SharedPreferences sp = null;
    private ProgressDialog pd = null;
    private TextView atools_tv_change_sms_backup = null;
    private TextView atools_tv_change_sms_restore = null;
    private TextView atools_tv_change_contacts_backup = null;
    private TextView atools_tv_change_contacts_restore = null;
    private TextView atools_tv_applock = null;
    private TextView atools_tv_common_num = null;



    //多线程下载号码归属地的数据库
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what){

                case SUCCESS:
                    Toast.makeText(AToolsActivity.this.getApplicationContext(), "数据库下载成功", Toast.LENGTH_SHORT).show();
                    break;

                case FAIL:
                    Toast.makeText(AToolsActivity.this.getApplicationContext(), "下载数据库失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.atools);
        this.tv_atool_query = (TextView)this.findViewById(R.id.atools_tv_query);
        this.tv_atool_query.setOnClickListener(this);
        this.tv_atools_address = (TextView) this.findViewById(R.id.tv_atools_address);
        this.atools_cb_address = (CheckBox) this.findViewById(R.id.atools_cb_address);
        this.atools_tv_select_bg = (TextView) this.findViewById(R.id.atools_tv_select_bg);
        this.atools_tv_change_location = (TextView) this.findViewById(R.id.atools_tv_change_location);
        this.atools_tv_change_sms_backup = (TextView) this.findViewById(R.id.atools_tv_change_sms_backup);
        this.atools_tv_change_sms_restore = (TextView) this.findViewById(R.id.atools_tv_change_sms_restore);
        this.atools_tv_change_contacts_backup = (TextView) this.findViewById(R.id.atools_tv_change_contacts_backup);
        this.atools_tv_change_contacts_restore = (TextView) this.findViewById(R.id.atools_tv_change_contacts_restore);
        this.atools_tv_applock = (TextView) this.findViewById(R.id.atools_tv_applock);
        this.atools_tv_common_num = (TextView) this.findViewById(R.id.atools_tv_common_num);
        this.serviceIntent = new Intent(this,AddressServiec.class);
        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        this.atools_cb_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            //当单选框选择被改变时候激活
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AToolsActivity.this.startService(serviceIntent);
                    AToolsActivity.this.tv_atools_address.setTextColor(Color.WHITE);
                    AToolsActivity.this.tv_atools_address.setText(AToolsActivity.this.getResources().getString(R.string.atool_tv_text_service));
                }else{
                    AToolsActivity.this.stopService(serviceIntent);
                    AToolsActivity.this.tv_atools_address.setTextColor(Color.RED);
                    AToolsActivity.this.tv_atools_address.setText(AToolsActivity.this.getResources().getString(R.string.atool_tv_text_noservice));
                }
            }
        });

        tv_atool_query.setOnClickListener(this);  //设置点击查询的监听
        this.atools_tv_select_bg.setOnClickListener(this); //设置选择背景的监听
        this.atools_tv_change_location.setOnClickListener(this);
        this.atools_tv_change_sms_backup.setOnClickListener(this);
        this.atools_tv_change_sms_restore.setOnClickListener(this);
        this.atools_tv_change_contacts_backup.setOnClickListener(this);
        this.atools_tv_change_contacts_restore.setOnClickListener(this);
        this.atools_tv_applock.setOnClickListener(this);
        this.atools_tv_common_num.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.atools_tv_query:       //当用户点击查询号码归属地的textview的时候激活
                this.pd = new ProgressDialog(this);

                if(isDBExit()){      //判断数据库是否存在
                    LogManagement.i(this.TAG, "进入号码查询界面");
                    Intent intent = new Intent(AToolsActivity.this,QueryNumberActivity.class);
                    this.startActivity(intent);
                    overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                }else{                //数据库不存在就链接服务器下载
                    LogManagement.i(this.TAG, "下载数据库");
                    this.pd.setMessage("正在下载数据库");
                    this.pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置显示风格是
                    this.pd.show();
                    final String path = this.getResources().getString(R.string.dburl);
                    final String filepath = "/sdcard/address.db";
                    new Thread(){ //开启一个新的线程，下载数据库
                        @Override
                        public void run() {
                            super.run();
                            try {
                                DownLoadFileTask.getFile(path, filepath, pd);
                                Message msg = new Message();
                                msg.what = SUCCESS;
                                pd.dismiss();
                                handler.sendMessage(msg);
                            } catch (Exception e) {
                                Message msg = new Message();
                                msg.what = FAIL;
                                pd.dismiss();
                                handler.sendMessage(msg);
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;


            case R.id.atools_tv_select_bg:     //选择显示来电归属地的背景
                AlertDialog.Builder builder = new Builder(this);
                builder.setTitle("归属地提示风格");
                String[] items = new String[]{
                        "半透明","活力橙","苹果绿"
                };
                builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("background", which);
                        editor.commit();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                builder.create().show();
                break;



            case R.id.atools_tv_change_location:       //改变显示来电归属地的显示位置
                Intent intent = new Intent(this,DragViewActivity.class);
                AToolsActivity.this.startActivity(intent);
                break;


            case R.id.atools_tv_change_sms_backup:     //备份短信
                Intent smsbackupIntent = new Intent(this,BackupSmsService.class);
                AToolsActivity.this.startService(smsbackupIntent);
                break;


            case R.id.atools_tv_change_sms_restore:    //短信还原
                final ProgressDialog pd = new ProgressDialog(AToolsActivity.this);  //动态创建一个ProgressDialog
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);     //设置ProgressDialog的显示风格
                pd.setMessage("短信还原");    //设置标题
                pd.show();                 //显示ProgressDialog
                final String sms_backup_path = this.getResources().getString(R.string.sms_backup_path);
                final SmsInfoService service = new SmsInfoService(this);
                final String restore_success = this.getResources().getString(R.string.sms_store_success);
                final String restore_fail = this.getResources().getString(R.string.sms_store_fail);

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            service.restoreSms(sms_backup_path, pd);//恢复短信
                            pd.dismiss();                           //取消ProgressDialog

                            //取消ProgressDialog
                            Looper.prepare();
                            Toast.makeText(AToolsActivity.this, restore_success, Toast.LENGTH_LONG).show();
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Looper.prepare();
                            Toast.makeText(AToolsActivity.this, restore_fail, Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    }
                }.start();
                break;


            case R.id.atools_tv_change_contacts_backup:  //联系人备份
                break;


            case R.id.atools_tv_change_contacts_restore:    //联系人还原
                break;



            case R.id.atools_tv_applock: //程序锁
                Intent applockIntent = new Intent(AToolsActivity.this,AppLockActivity.class);
                AToolsActivity.this.startActivity(applockIntent);
                break;


            case R.id.atools_tv_common_num: //常用号码查询
                Intent commonNumIntent = new Intent(AToolsActivity.this,CommonNumberActivity.class);
                AToolsActivity.this.startActivity(commonNumIntent);
                break;


        }
    }


    //判断本地号码归属地数据库是否存在
    public boolean isDBExit(){
        File file = new File("/sdcard/address.db");
        boolean flag = file.exists();
        System.out.println(flag);
        if(flag){
            return true;
        }else{
            return false;
        }
    }


}
