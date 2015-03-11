package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.service.IService;
import com.example.androiddefprot.service.WatchDogService;
import com.example.androiddefprot.util.MD5Encoder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: LockScreenActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 程序锁Activity，每当服务检测到启动的应用已经加锁了，就弹出这个Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "LockScreenActivity";
    private ImageView app_lock_pwd_iv_icon = null;
    private TextView app_lock_pwd_tv_name = null;
    private EditText app_lock_pwd_et = null;
    private Button app_lock_pwd_bt = null;
    private SharedPreferences sp = null;
    private String realPwd = null;
    private Intent lockScreenIntent = null;
    private String packagename = null;
    private IService iService = null;
    private MyConn myConn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.app_lock_pwd);
        this.myConn = new MyConn();
        this.app_lock_pwd_iv_icon = (ImageView) this.findViewById(R.id.app_lock_pwd_iv_icon);
        this.app_lock_pwd_tv_name = (TextView) this.findViewById(R.id.app_lock_pwd_tv_name);
        this.app_lock_pwd_et = (EditText) this.findViewById(R.id.app_lock_pwd_et);
        this.app_lock_pwd_bt = (Button) this.findViewById(R.id.app_lock_pwd_bt);
        this.sp = this.getSharedPreferences("config",Context.MODE_PRIVATE);
        Intent intent = new Intent(LockScreenActivity.this,WatchDogService.class);
        bindService(intent, myConn, Context.BIND_AUTO_CREATE);
        lockScreenIntent = this.getIntent();
        packagename = lockScreenIntent.getStringExtra("packagename");
        app_lock_pwd_bt.setOnClickListener(new OnConfirmListener());
        ApplicationInfo appInfo;
        try {
            appInfo = getPackageManager().getPackageInfo(packagename, 0).applicationInfo;
            app_lock_pwd_iv_icon.setImageDrawable(appInfo.loadIcon(getPackageManager()));
            app_lock_pwd_tv_name.setText(appInfo.loadLabel(getPackageManager()));

        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    /**
     * 把返回按钮的功能取消
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(event.getKeyCode()== KeyEvent.KEYCODE_BACK){
            return true;//阻止按钮事件继续分法下去
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 确定按钮点击事件
     */
    private class OnConfirmListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            String password = app_lock_pwd_et.getText().toString().trim();
            if(TextUtils.isEmpty(password)){
                Toast.makeText(LockScreenActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                return;
            }else{
                LockScreenActivity.this.realPwd = LockScreenActivity.this.sp.getString("password", null);
                if(MD5Encoder.encode(password).equals(realPwd)){
                    LogManagement.i(TAG, "进入被锁的程序");
                    //当用户输入密码正确，要通知WatchDogService 看门狗 暂时取消对这个程序的保护
                    iService.callAppProtectStop(packagename);
                    finish();
                }else{
                    Toast.makeText(LockScreenActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


    /**
     * 自定义ServiceConnection类
     * @author jake
     *
     */
    private class MyConn implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iService = (IService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
        }
    }


    //解除绑定
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unbindService(myConn);
    }
}
