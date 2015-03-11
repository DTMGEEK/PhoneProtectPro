package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;
import com.example.androiddefprot.receiver.MyDeviceAdminReciver;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 *
 * @Title: SetupGuideFourthActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 手机防盗设置第四步
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SetupGuideFourthActivity extends Activity implements OnClickListener {

    private CheckBox cb_setupguide_isprotecting = null;
    private Button bt_setupguide_fourth_finish = null;
    private Button bt_setupguide_fourth_previous = null;
    private SharedPreferences sp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setupguidefour);
        //初始化SharePreference和控件
        this.cb_setupguide_isprotecting = (CheckBox) this.findViewById(R.id.cb_setupguide_isprotecting);
        this.bt_setupguide_fourth_finish = (Button) this.findViewById(R.id.bt_setupguide_fourth_finish);
        this.bt_setupguide_fourth_previous = (Button) this.findViewById(R.id.bt_setupguide_fourth_previous);

        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        //判断手机是否处于保护状态 并初始化CheckBox
        boolean isprotecting = sp.getBoolean("isprotecting", false);
        if(isprotecting){
            this.cb_setupguide_isprotecting.setChecked(true);
            this.cb_setupguide_isprotecting.setText("手机防盗保护中");
        }

        //监听CheckBox状态的改变，一但改变马上修改SharePreference 里的 isprotecting判断手机是否受到保护。
        this.cb_setupguide_isprotecting.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = null;
                if(isChecked){
                    cb_setupguide_isprotecting.setChecked(true);
                    cb_setupguide_isprotecting.setText("手机防盗保护中");
                    editor = SetupGuideFourthActivity.this.sp.edit();
                    editor.putBoolean("isprotecting", true);
                    editor.commit();
                }else{
                    cb_setupguide_isprotecting.setChecked(false);
                    cb_setupguide_isprotecting.setText("没有开启防盗保护");
                    editor = SetupGuideFourthActivity.this.sp.edit();
                    editor.putBoolean("isprotecting", false);
                    editor.commit();
                }
            }
        });

        bt_setupguide_fourth_finish.setOnClickListener(this);
        bt_setupguide_fourth_previous.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_setupguide_fourth_finish:
                if(this.cb_setupguide_isprotecting.isChecked()){
                    this.finish();
                    finishSetup();
                }else{
                    Dialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提醒")
                            .setMessage("强烈建议开启手机防盗,是否完成设置?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SetupGuideFourthActivity.this.finish();
                                    finishSetup();
                                }
                            })
                            .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create();
                    dialog.show();
                }
                break;

            case R.id.bt_setupguide_fourth_previous:
                this.finish();
                Intent intent = new Intent(SetupGuideFourthActivity.this,SetupGuideThirdActivity.class);
                this.startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;
        };
    }


    //结束安装
    private void finishSetup() {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putBoolean("isprotecting", true);
        editor.putBoolean("issteupalready", true);
        editor.commit();

        //注册device Admin 应用-------就是获得手机的超级用户权限
        DevicePolicyManager manager = (DevicePolicyManager)this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mAdminName = new ComponentName(this,MyDeviceAdminReciver.class);
        if(!manager.isAdminActive(mAdminName)){
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminName);
            this.startActivity(intent);
        }
    }

}
