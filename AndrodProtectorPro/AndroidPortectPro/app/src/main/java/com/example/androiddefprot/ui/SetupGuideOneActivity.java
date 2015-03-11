package com.example.androiddefprot.ui;



import com.example.androiddefprot.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 *
 * @Title: SetupGuideOneActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 防盗配置第一步
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class SetupGuideOneActivity extends Activity implements OnClickListener {

    private Button bt_setupguide_next = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setupguideone);
        this.bt_setupguide_next = (Button) this.findViewById(R.id.bt_setupguide_first_next);
        this.bt_setupguide_next.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.bt_setupguide_first_next:
                Intent setupGuideIntent = new Intent(SetupGuideOneActivity.this,SetupGuideSecondActivity.class);

                SetupGuideOneActivity.this.finish();    //把当前Activity从任务栈中移除

                SetupGuideOneActivity.this.startActivity(setupGuideIntent);
                //设置切换Activity时候的动画效果
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }
}
