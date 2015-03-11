package com.example.androiddefprot.ui;

import com.example.androiddefprot.R;
import com.example.androiddefprot.engine.NumberAddressService;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: QueryNumberActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 查询号码归属地Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class QueryNumberActivity extends Activity {

    private static final String TAG = "QueryNumberActivity";
    private EditText et_query_number = null;
    private TextView tv_query_number_address = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.query_number);
        this.et_query_number = (EditText) this.findViewById(R.id.et_query_number);
        //this.tv_query_number_address = (TextView) this.findViewById(R.id.tv_query_number_address);
    }


    /**
     * 查询号码归属地
     * @param view
     */
    public void query(View view){
        String number = this.et_query_number.getText().toString().trim();
        if(TextUtils.isEmpty(number)){
            Toast.makeText(this, "请输入号码", Toast.LENGTH_LONG).show();
        }else{
            String address = NumberAddressService.getAddressByNumber(number);
            LogManagement.i(TAG, "号码归属地是" + address);
            Toast.makeText(this, "号码归属地是" + address, Toast.LENGTH_LONG).show();
        }
    }


}
