package com.example.androiddefprot.ui;

import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.ContactInfo;
import com.example.androiddefprot.engine.ContactInfoService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;



/**
 *
 * @Title: ContactInfoActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 选择联系人Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class ContactInfoActivity extends Activity {

    private ListView listView = null;
    private List<ContactInfo> infos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.select_contact);
        this.listView = (ListView) this.findViewById(R.id.lv_select_contact);
        ContactInfoService service = new ContactInfoService(this);     //调用查找通讯录的服务。
        infos = service.getContactInfos();
        SelectContactAdapter adapter = new SelectContactAdapter();     //为ListView设置Adapter
        this.listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String phone = infos.get(position).getPhone();
                Intent phoneIntent = new Intent();
                phoneIntent.putExtra("number", phone);              //把电话号码封装到Intetn里面 以便在回调函数中获取数据
                setResult(0,phoneIntent);
                finish();
            }
        });
    }



    class SelectContactAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int position) {
            return infos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        //返回Adapter每个项项的View
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ContactInfo info = infos.get(position);
            LinearLayout layout = new LinearLayout(ContactInfoActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView tv_name = new TextView(ContactInfoActivity.this);
            tv_name.setText(info.getName());
            tv_name.setTextColor(Color.WHITE);
            TextView tv_phone = new TextView(ContactInfoActivity.this);
            tv_phone.setText(info.getPhone());
            tv_phone.setTextColor(Color.WHITE);
            layout.addView(tv_name);
            layout.addView(tv_phone);
            return layout;
        }
    }


}
