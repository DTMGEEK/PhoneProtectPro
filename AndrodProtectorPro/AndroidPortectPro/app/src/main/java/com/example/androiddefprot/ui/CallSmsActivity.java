package com.example.androiddefprot.ui;

import java.util.List;

import junit.runner.BaseTestRunner;

import com.example.androiddefprot.Gesture.ListViewSwipeGesture;
import com.example.androiddefprot.R;
import com.example.androiddefprot.db.dao.BlackNumberDao;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/**
 *
 * @Title: CallSmsActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 黑名单管理Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class CallSmsActivity extends Activity {

    private Button add_black_number_bn = null;
    private ListView call_sms_safe_lv = null;
    private BlackNumberDao blacknumberdao = null; //数据库服务类进行CRUD操作
    private List<String> numbers = null;          //保存号码
    private CallAdapter adapter = null;			  //自定义数据适配器（adapter）避免使用ArrayAdapter的缓存
    private View BlackNumber_DialogView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       this.setContentView(R.layout.call_sms_safe);
        //faddingList();
        this.blacknumberdao = new BlackNumberDao(this);
        this.add_black_number_bn = (Button) this.findViewById(R.id.add_black_number_bn);
        this.call_sms_safe_lv = (ListView) this.findViewById(R.id.call_sms_safe_lv);

        this.registerForContextMenu(call_sms_safe_lv);//在ListVIew控件上注册一个上下文菜单




        add_black_number_bn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Builder myBuider = myBuider(CallSmsActivity.this);
                final AlertDialog alertDialog =  myBuider.show();
                //点击屏幕外侧，dialog不消失
                alertDialog.setCanceledOnTouchOutside(false);


              /*  AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
                builder.setTitle("添加黑名单号码");
                final EditText editText = new EditText(CallSmsActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(editText);
                builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String number = editText.getText().toString().trim();
                        if(TextUtils.isEmpty(number)){
                            Toast.makeText(CallSmsActivity.this,CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null) , Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            blacknumberdao.add(number);

                            //要更新ListView可以有两种方法
                            // 1.重新建立一个新的Adapter（不推荐使用）
                            //todo: 通知listview更新数据
                            // 缺点: 重新刷新整个listview
                            //numbers = dao.getAllNumbers();
                            //lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));


                            //2.通知数据适配器更新数据
                            numbers = blacknumberdao.getAllNumber();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });*/

              /*  builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();*/


                //弹出的dialog监听确认
                Button blacknumber_dialog_confirm_btn = (Button) BlackNumber_DialogView.findViewById(R.id.blacknumber_dialog_confirm_btn);
                blacknumber_dialog_confirm_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText editText = (EditText) BlackNumber_DialogView.findViewById(R.id.balck_number_et);
                        String number = editText.getText().toString().trim();
                        if(TextUtils.isEmpty(number)){
                            Toast.makeText(CallSmsActivity.this,CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null) , Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            blacknumberdao.add(number);

                            //要更新ListView可以有两种方法
                            // 1.重新建立一个新的Adapter（不推荐使用）
                            //todo: 通知listview更新数据
                            // 缺点: 重新刷新整个listview
                            //numbers = dao.getAllNumbers();
                            //lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));


                            //2.通知数据适配器更新数据
                            numbers = blacknumberdao.getAllNumber();
                            adapter.notifyDataSetChanged();
                        }
                        alertDialog.dismiss();
                    }

                });

                //弹出的dialog监听取消
                Button blacknumber_dialog_cancel_btn = (Button) BlackNumber_DialogView.findViewById(R.id.blacknumber_dialog_cancel_btn);
                blacknumber_dialog_cancel_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });






        numbers = blacknumberdao.getAllNumber();

        adapter = new CallAdapter();
        this.call_sms_safe_lv.setAdapter(adapter);

        //设置滑动监听
        final ListViewSwipeGesture touchListener = new ListViewSwipeGesture(
                call_sms_safe_lv, swipeListener, this);
        touchListener.SwipeType	=	ListViewSwipeGesture.Double;    //设置两个选项列表项的背景
        call_sms_safe_lv.setOnTouchListener(touchListener);
    }



    /*使用ArrayAdapter 时候 存在缓存所以在使用adapter.notifyDataSetChanged()的时候，会出现没办法动态更
    新ArrayAdapter的数据问题所以，要解决这个问题，应该通过继承BaseAdapter   重写里面的方法，从而避免ArrayAdapter的缓存问题。*/
    private class CallAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return numbers.size();
        }


        @Override
        public Object getItem(int position) {
            return numbers.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
          /*  View view = View.inflate(CallSmsActivity.this, R.layout.blacknumber_item, null);
            TextView tv = (TextView) view.findViewById(R.id.blacknumber_item_tv);
            tv.setText(numbers.get(position));
            return view;*/
            if (null == convertView) {
                convertView = View.inflate(CallSmsActivity.this, R.layout.blacknumber_item_parent, null);
            }
            ImageView ManImg=(ImageView)convertView.findViewById(R.id.ManImg);
            TextView blacknumber_item_tv=(TextView)convertView.findViewById(R.id.blacknumber_item_tv);
            blacknumber_item_tv.setText(numbers.get(position).toString());
            return convertView;
        }
    }



    /**
     * 创建一个上下文菜单
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.getMenuInflater(); //把menu资源转换为一个menu对象
        inflater.inflate(R.menu.context_menu, menu);
    }


    /**
     * 当上下文菜单某个条目被选中后，激活的方法
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info =	(AdapterContextMenuInfo)item.getMenuInfo();
        int id = (int) info.id;
        String number = numbers.get(id);
        switch(item.getItemId()){
            case R.id.update_blacknumber:
                updateNumber(number);
                break;

            case R.id.delete_blacknumber:
                //当前条目id
                blacknumberdao.delete(number);
                //重新获取电话黑名单
                numbers = blacknumberdao.getAllNumber();
                //通知listview更新数据
                adapter.notifyDataSetChanged();
                break;
        }
        return false;
    }



    /**
     * 用户更新黑名单数据库方法
     */
    private void updateNumber(final String oldNumber) {
        final EditText et = new EditText(CallSmsActivity.this);
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        AlertDialog.Builder builder = new Builder(CallSmsActivity.this);
        builder.setTitle("更改黑名单号码");
        builder.setView(et);
        builder.setPositiveButton("更改", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNumber = et.getText().toString().trim();
                if(TextUtils.isEmpty(newNumber)){
                    Toast.makeText(CallSmsActivity.this, CallSmsActivity.this.getResources().getString(R.string.call_sms_safe_not_null), Toast.LENGTH_LONG).show();
                    return;
                }else{
                    blacknumberdao.update(oldNumber, newNumber);

                    //要更新ListView可以有两种方法
                    // 1.重新建立一个新的Adapter（不推荐使用）
                    //todo: 通知listview更新数据
                    // 缺点: 重新刷新整个listview
                    //numbers = dao.getAllNumbers();
                    //lv_call_sms_safe.setAdapter(new ArrayAdapter<String>(CallSmsActivity.this, R.layout.blacknumber_item, R.id.tv_blacknumber_item, numbers));


                    //2.通知数据适配器更新数据
                    numbers = blacknumberdao.getAllNumber();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
       // builder.create().show();
    }


    ListViewSwipeGesture.TouchCallbacks swipeListener = new ListViewSwipeGesture.TouchCallbacks() {

        @Override
        public void FullSwipeListView(int position) {
            // TODO Auto-generated method stub
            Toast.makeText(CallSmsActivity.this, "Action_2", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void HalfSwipeListView(int position) {
            // TODO Auto-generated method stub
//            System.out.println("<<<<<<<" + position);
            //当前条目id
            blacknumberdao.delete(numbers.get(position));
            //重新获取电话黑名单
            numbers = blacknumberdao.getAllNumber();
            //通知listview更新数据
            adapter.notifyDataSetChanged();
            Toast.makeText(CallSmsActivity.this,"删除", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void LoadDataForScroll(int count) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
            // TODO Auto-generated method stub
//            Toast.makeText(activity,"Delete", Toast.LENGTH_SHORT).show();
//            for(int i:reverseSortedPositions){
//                data.remove(i);
//                new MyAdapter().notifyDataSetChanged();
//            }
        }

        @Override
        public void OnClickListView(int position) {
            // TODO Auto-generated method stub

        }

    };


    public Builder myBuider(Context context){
        this.BlackNumber_DialogView = this.getLayoutInflater().inflate(R.layout.balcknumber_dialog,null);
        AlertDialog.Builder builder = new Builder(context);
        builder.setView(BlackNumber_DialogView);
        return builder;
    }














   /* //faddingList开源控件
    public void faddingList(){

        FadingActionBarHelper helper = new FadingActionBarHelper()
                                .actionBarBackground(R.drawable.ab_background)
                                .headerLayout(R.layout.headerlayout)
                                .contentLayout(R.layout.call_sms_safe);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        for (int i = 0; i < 40; i++) {
            list.add("test1");
        }

        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new mAdapter());

    }



    //faddingList开源控件
    class mAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){

                view = getLayoutInflater().inflate(R.layout.blacknumber_item,null);
                TextView title = (TextView) view.findViewById(R.id.title);

                title.setText(list.get(i));
            }
            return view;
        }
    }*/






}
