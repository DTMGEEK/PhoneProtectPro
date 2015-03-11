package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.example.androiddefprot.util.MD5Encoder;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * @Title: LostProtectedActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description:  手机防盗Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class LostProtectedActivity extends Activity implements OnClickListener {

    private static final String TAG = "LostProtectedActivity";
    private SharedPreferences sp = null;
    private Dialog dialog = null;
    private EditText et_pwd;
    private EditText et_pwd_confirm;
    private TextView tv_lost_protected_number = null;
    private CheckBox cb_isprotecting = null;
    private TextView tv_reentry_setup_guide = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        if(isPWDSteup()){
            LogManagement.i(this.TAG, "设置了密码,正常登陆的对话框");
            showNormalEntryDialog();
        }else{
            LogManagement.i(this.TAG, "没有设置密码,显示第一次对话框");
            showFirstEntryDialog();
        }
    }



    /**
     * 显示正常登陆对话框
     */
    private void showNormalEntryDialog() {
        this.dialog = new Dialog(this,R.style.mydialog);
        View view = View.inflate(this, R.layout.normal_entry_dialog, null);
        this.et_pwd = (EditText) view.findViewById(R.id.et_normal_entry_dialog_pwd);
        Button bt_normal_ok = (Button) view.findViewById(R.id.bt_normal_dialog_ok);
        Button bn_normal_cancel = (Button) view.findViewById(R.id.bt_normal_dialog_cancel);
        bt_normal_ok.setOnClickListener(this);
        bn_normal_cancel.setOnClickListener(this);
        //点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);


        dialog.setContentView(view);
        dialog.show();
    }



    /**
     * 首次进入手机防盗弹出对话框 要求用户输入密码
     */
    private void showFirstEntryDialog() {
        this.dialog = new Dialog(this,R.style.mydialog);

        View view = View.inflate(this, R.layout.first_entry_dialog, null);
        this.et_pwd = (EditText) view.findViewById(R.id.et_first_entry_dialog_pwd);
        this.et_pwd_confirm = (EditText) view.findViewById(R.id.et_first_entry_dialog_pwd_confirm);
        Button bt_first_dialog_ok = (Button) view.findViewById(R.id.bt_first_dialog_ok);
        Button bt_first_dialog_cancel = (Button) view.findViewById(R.id.bt_first_dialog_cancel);
        bt_first_dialog_ok.setOnClickListener(this);
        bt_first_dialog_cancel.setOnClickListener(this);
        //点击屏幕外侧，dialog不消失
        dialog.setCanceledOnTouchOutside(false);


        dialog.setContentView(view);
        dialog.show();
    }



    /**
     * 判断SharedPreferences的xml文件里是否有password 有显示输入密码对话框，没有表示第一次
     * 进入，要求输入密码。
     */
    private boolean isPWDSteup() {
        String pwd = this.sp.getString("password", null);
        if(null == pwd){
            return false;
        }else{
            if("".equals(pwd)){
                return false;
            }else{
                return true;
            }
        }
    }




    /**
     * 通过id判断 用户点击了哪个按钮 再在各个不同的按钮触发不同的事件。
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()){

            case R.id.bt_first_dialog_cancel:
                this.dialog.dismiss();
                intent = new Intent(LostProtectedActivity.this,MainScreenActivity.class);
                this.startActivity(intent);
                this.finish();
                break;

            case R.id.bt_first_dialog_ok:
                if("".equals(this.et_pwd.getText().toString().trim())||"".equals(this.et_pwd_confirm.getText().toString())){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(this.et_pwd.getText().toString().trim().equals(this.et_pwd_confirm.getText().toString())){
                        SharedPreferences.Editor editor = this.sp.edit();
                        editor.putString("password", MD5Encoder.encode(this.et_pwd.getText().toString()));
                        editor.commit();
                        LogManagement.i(this.TAG, "第一次设置进入防盗设置界面");
                        isEntrySetupActivity();
                    }else{
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                break;

            case R.id.bt_normal_dialog_cancel:
                this.dialog.dismiss();
                intent = new Intent(LostProtectedActivity.this,MainScreenActivity.class);
                this.startActivity(intent);
                this.finish();
                break;

            //普通正常登陆手机防盗设置界面
            case R.id.bt_normal_dialog_ok:
                String password = this.et_pwd.getText().toString().trim();

                if("".equals(this.et_pwd.getText().toString())){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }else{
                    String realpwd = this.sp.getString("password", null);
                    if(realpwd.equals(MD5Encoder.encode(password))){
                        LogManagement.i(this.TAG, "进入防盗界面");
                        isEntrySetupActivity();
                        this.setContentView(R.layout.lost_protected);
                        this.tv_lost_protected_number = (TextView) this.findViewById(R.id.tv_lost_protected_number);
                        this.cb_isprotecting = (CheckBox) this.findViewById(R.id.cb_isprotecting);
                        this.tv_reentry_setup_guide = (TextView) this.findViewById(R.id.tv_reentry_setup_guide);
                        this.tv_lost_protected_number.setText("安全号码是："+this.sp.getString("safenumber", ""));
                        this.tv_reentry_setup_guide.setOnClickListener(this);
                        boolean isprotect = sp.getBoolean("isprotecting", false);
                        //初始化CheckBox
                        if(isprotect){
                            this.cb_isprotecting.setText("手机防盗保护中");
                            this.cb_isprotecting.setChecked(true);
                        }

                        this.cb_isprotecting.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(isChecked){
                                    cb_isprotecting.setChecked(true);
                                    cb_isprotecting.setText("手机防盗保护中");
                                    SharedPreferences.Editor editor = LostProtectedActivity.this.sp.edit();
                                    editor.putBoolean("isprotecting", true);
                                    editor.commit();
                                }else{
                                    cb_isprotecting.setChecked(true);
                                    cb_isprotecting.setText("没有开启手机防盗");
                                    SharedPreferences.Editor editor = LostProtectedActivity.this.sp.edit();
                                    editor.putBoolean("isprotecting", false);
                                    editor.commit();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    this.dialog.dismiss();
                }
                break;

            //重新进入设置向导
            case R.id.tv_reentry_setup_guide:
                Intent resetIntent = new Intent(LostProtectedActivity.this,SetupGuideOneActivity.class);
                LostProtectedActivity.this.startActivity(resetIntent);
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                break;
        }
    }


    /**
     * 第一次进入防盗要进入设置界面
     */
    private void isEntrySetupActivity() {
        if(!this.isSetUp()){
            Intent setupIntent = new Intent(this,SetupGuideOneActivity.class);
            this.startActivity(setupIntent);
        }
    }



    /**
     * 判断是否进行过设置了
     * @return
     */
    private boolean isSetUp(){
        return this.sp.getBoolean("issteupalready", false);
    }


}
