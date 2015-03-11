package com.example.androiddefprot.ui;

import javax.net.ssl.ManagerFactoryParameters;

import com.example.androiddefprot.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


/**
 *
 * @Title: SetupGuideSecondActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description:  防盗设置第二个Activity，绑定sim卡序列号，实现防止手机被盗后换卡。
 * @author lian_weijian@163.com
 * @version V1.0
 */

public class SetupGuideSecondActivity extends Activity implements OnClickListener {

    private Button bt_setupguide_second_next = null;
    private Button bt_setupguide_second_previous = null;
    private Button bt_bind = null;
    private CheckBox cb_bind = null;
    private SharedPreferences sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setupguidetwo);
        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        this.bt_bind = (Button) this.findViewById(R.id.bt_bind);
        this.bt_setupguide_second_next = (Button) this.findViewById(R.id.bt_setupguide_second_next);
        this.bt_setupguide_second_previous = (Button) this.findViewById(R.id.bt_setupguide_second_previous);
        this.cb_bind = (CheckBox) this.findViewById(R.id.cb_bind);


        //初始化checkbox状态
        String sim = this.sp.getString("sim", null);
        if(sim != null){
            this.cb_bind.setText("已经绑定");
            this.cb_bind.setChecked(true);
        }else{
            this.cb_bind.setText("没有绑定");
            this.cb_bind.setChecked(false);
            resetSimInfo();
        }
        this.cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SetupGuideSecondActivity.this.cb_bind.setText("已经绑定");
                    SetupGuideSecondActivity.this.setSimInfo();
                }else{
                    SetupGuideSecondActivity.this.cb_bind.setText("没有绑定");
                    resetSimInfo();
                }
            }
        });

        this.bt_bind.setOnClickListener(this);
        this.bt_setupguide_second_next.setOnClickListener(this);
        this.bt_setupguide_second_previous.setOnClickListener(this);
    }


    /**
     * 判断用户点击了哪一个按钮。
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_bind:
                setSimInfo();
                break;

            case R.id.bt_setupguide_second_next:
                Intent intent = new Intent(SetupGuideSecondActivity.this,SetupGuideThirdActivity.class);
                SetupGuideSecondActivity.this.finish();
                SetupGuideSecondActivity.this.startActivity(intent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);//使用activity之间的切换动画
                break;

            case R.id.bt_setupguide_second_previous:
                Intent intentse = new Intent(SetupGuideSecondActivity.this,SetupGuideOneActivity.class);
                SetupGuideSecondActivity.this.finish();
                SetupGuideSecondActivity.this.startActivity(intentse);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);//使用activity之间的切换动画
                break;
        }
    }




    /**
     * 把sim卡串口放入到SharePreference的xml文件中
     */
    private void setSimInfo() {
        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String simsernumber = manager.getSimSerialNumber();
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString("sim", simsernumber);
        editor.commit();
    }



    /**
     * sim卡绑定的串口，取消掉
     */
    private void resetSimInfo() {
        SharedPreferences.Editor editor = SetupGuideSecondActivity.this.sp.edit();
        editor.putString("sim", null);
        editor.commit();
    }

}
