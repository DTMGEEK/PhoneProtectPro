package com.example.androiddefprot.ui;



import com.example.androiddefprot.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 *
 * @Title: SetupGuideThirdActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 配置防盗第三步，设置防盗号码，手机sim卡更换后会发送短信到预先设置好的号码。
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SetupGuideThirdActivity extends Activity implements OnClickListener{


    private EditText et_setup3_number = null;
    private Button bt_select_thrid_contact = null;
    private Button bt_setupguide_third_previous = null;
    private Button bt_setupguide_third_next = null;
    private SharedPreferences sp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setupguidethree);
        this.sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);

        this.et_setup3_number = (EditText) this.findViewById(R.id.et_setup3_number);
        this.bt_select_thrid_contact = (Button) this.findViewById(R.id.bt_select_thrid_contact);
        this.bt_setupguide_third_previous = (Button) this.findViewById(R.id.bt_setupguide_third_previous);
        this.bt_setupguide_third_next = (Button) this.findViewById(R.id.bt_setupguide_third_next);

        //设置按钮监听
        this.bt_select_thrid_contact.setOnClickListener(this);
        this.bt_setupguide_third_next.setOnClickListener(this);
        this.bt_setupguide_third_previous.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

            //点击选择联系人按钮
            case R.id.bt_select_thrid_contact:
                Intent intent = new Intent(SetupGuideThirdActivity.this,ContactInfoActivity.class);
                SetupGuideThirdActivity.this.startActivityForResult(intent, 0);
                break;

            //点击下一步按钮，跳转到下一步的Activity
            case R.id.bt_setupguide_third_next:
                String number = et_setup3_number.getText().toString().trim();
                if("".equals(number)){
                    Toast.makeText(SetupGuideThirdActivity.this, "安全号码不能为空" , Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    SharedPreferences.Editor editor = SetupGuideThirdActivity.this.sp.edit();
                    editor.putString("safenumber", number);
                    editor.commit();
                }
                Intent nextIntent = new Intent(SetupGuideThirdActivity.this,SetupGuideFourthActivity.class);
                SetupGuideThirdActivity.this.startActivity(nextIntent);
                overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
                this.finish();
                break;

            //点击上一步按钮，回到上一个activity
            case R.id.bt_setupguide_third_previous:
                Intent preIntent = new Intent(SetupGuideThirdActivity.this,SetupGuideSecondActivity.class);
                SetupGuideThirdActivity.this.startActivity(preIntent);
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                this.finish();
                break;
        }
    }


    //设置Intent的回调监听，
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String number = data.getStringExtra("number");
            SetupGuideThirdActivity.this.et_setup3_number.setText(number);
        }
    }

}
