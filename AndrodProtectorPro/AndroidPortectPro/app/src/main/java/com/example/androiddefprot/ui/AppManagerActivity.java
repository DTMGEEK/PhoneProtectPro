package com.example.androiddefprot.ui;

import java.util.ArrayList;
import java.util.List;
import com.example.androiddefprot.R;
import com.example.androiddefprot.adapter.AppManagerAdapter;
import com.example.androiddefprot.domain.AppInfo;
import com.example.androiddefprot.engine.AppInfoProvider;
import com.example.androiddefprot.logmanagement.LogManagement;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;



/**
 *
 * @Title: AppManagerActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description:  程序管理器Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppManagerActivity extends ActionBarActivity implements OnClickListener,OnDismissCallback {

    private static final int GET_ALL_APP_FINISH = 10;
    private static final int GET_ALL_USER_APP_FINISH = 11;
    private static final String TAG = "AppManagerActivity";
    private TextView app_manager_tv_title = null;
    private ListView app_manager_lv = null;
    private LinearLayout app_manager_ll_loading = null;
    private List<AppInfo> appInfos = null; //保存所以app的信息
    private List<AppInfo> userAppInfos = null; //保存用户app的信息
    private AppManagerAdapter adapter = null;  //数据适配器
    private PopupWindow popupWindow = null;     //弹出窗口
    private boolean isloading = false; //判断是不是加载完成了
    private LinearLayout app_manager_tv_ll_title = null;


    //cardList
    private SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = null;


    private Handler handler = new Handler(){         //启动一个新的线程，去获得所有app的信息
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GET_ALL_APP_FINISH:      //获取所有的应用程序
                    adapter = new AppManagerAdapter(AppManagerActivity.this,appInfos);

                    //cardlist
                    swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                            new SwipeDismissAdapter(adapter, AppManagerActivity.this));
                    //cardlist
                    swingBottomInAnimationAdapter.setAbsListView(app_manager_lv);

                    //cardlist
                    app_manager_lv.setAdapter(swingBottomInAnimationAdapter);


                    //app_manager_lv.setAdapter(adapter);
                    app_manager_ll_loading.setVisibility(View.INVISIBLE);
                    AppManagerActivity.this.isloading = false;
                    break;

                case GET_ALL_USER_APP_FINISH:   //获取完用户自己安装的程序
                    adapter = new AppManagerAdapter(AppManagerActivity.this,userAppInfos);

                    //cardlist
                    swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                            new SwipeDismissAdapter(adapter, AppManagerActivity.this));
                    //cardlist
                    swingBottomInAnimationAdapter.setAbsListView(app_manager_lv);

                    //cardlist
                    app_manager_lv.setAdapter(swingBottomInAnimationAdapter);

                    //app_manager_lv.setAdapter(adapter);
                    app_manager_ll_loading.setVisibility(View.INVISIBLE);
                    AppManagerActivity.this.isloading = false;
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setContentView(R.layout.app_manager);
        this.app_manager_tv_title = (TextView) this.findViewById(R.id.app_manager_tv_title);
        this.app_manager_tv_ll_title = (LinearLayout) this.findViewById(R.id.app_manager_tv_ll_title);
        //所以应用程序和用户应用程序标题点击事件
        AppManagerActivity.this.app_manager_tv_ll_title.setOnClickListener(AppManagerActivity.this);//如果要触发这个事件监听必须在这里

        this.app_manager_lv = (ListView) this.findViewById(R.id.app_manager_lv);
        this.app_manager_ll_loading = (LinearLayout) this.findViewById(R.id.app_manager_ll_loading);
        this.app_manager_ll_loading.setVisibility(View.VISIBLE);

        initUI(true);//初始化或者更新listView

        //当单击ListView的每一个项时，弹出popupwindow
        app_manager_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                dismissPopWin();

                int[] arrayInt = new int[2];
                view.getLocationInWindow(arrayInt);

                int i = arrayInt[0] + 60;
                int j = arrayInt[1] - 15;

                AppInfo appinfo = (AppInfo) app_manager_lv.getItemAtPosition(position);

                View popupview = View.inflate(AppManagerActivity.this, R.layout.popup_item, null); //得到popupwindow的布局文件实例

                LinearLayout ll_start = (LinearLayout) popupview.findViewById(R.id.ll_start);
                LinearLayout ll_uninstall = (LinearLayout) popupview.findViewById(R.id.ll_uninstall);
                LinearLayout ll_share = (LinearLayout) popupview.findViewById(R.id.ll_share);

                //设置popupwindow的监听器
                ll_start.setOnClickListener(AppManagerActivity.this);
                ll_uninstall.setOnClickListener(AppManagerActivity.this);
                ll_share.setOnClickListener(AppManagerActivity.this);


                //设置Tag标签，可以标识是单击了一个app
                ll_start.setTag(position);
                ll_uninstall.setTag(position);
                ll_share.setTag(position);


                //设置popupwindow的弹出动画
                LinearLayout ll_popup = (LinearLayout) popupview.findViewById(R.id.ll_popup);
                ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
                sa.setDuration(200);


                //实例化popupwindow
                popupWindow = new PopupWindow(popupview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                Drawable background = AppManagerActivity.this.getResources().getDrawable(R.drawable.local_popup_bg); //设置popupwindow的背景，一定要设置背景不然会出现很多奇怪的问题
                popupWindow.setBackgroundDrawable(background); //设置背景
                popupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP, i, j); //设置popupwindow的出现位置
                ll_popup.startAnimation(sa);  //启动动画

            }
        });


        //监听listview滚动时候状态的改变
        app_manager_lv.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                dismissPopWin();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                dismissPopWin();
            }
        });
    }


    /**
     * 初始化或者更新ListView线程
     */
    private void initUI(final boolean flag) {
        this.app_manager_ll_loading.setVisibility(View.VISIBLE);
        new Thread(){       //启动一个新线程，来获取所有app信息
            @Override
            public void run() {
                AppManagerActivity.this.isloading = true;
                if(flag){
                    appInfos = new AppInfoProvider(AppManagerActivity.this).getAllApp();
                    Message msg = new Message();
                    msg.what = GET_ALL_APP_FINISH;
                    handler.sendMessage(msg);
                }else{//只是获得用户安装的应用程序
                    appInfos = new AppInfoProvider(AppManagerActivity.this).getAllApp();
                    AppManagerActivity.this.userAppInfos = getUserApp(appInfos);
                    Message msg = new Message();
                    msg.what = GET_ALL_USER_APP_FINISH;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }



    /**
     * 关闭popupwindow
     */
    private void dismissPopWin() {
        if(popupWindow != null){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }


    /**
     * 判断用户点击popupwind的哪一个选项
     */
    @Override
    public void onClick(View v) {
        LogManagement.i(TAG, "hello");
        if(AppManagerActivity.this.isloading){
            return;
        }
        int positon = 0;
        if(v.getTag() != null){
            positon = (Integer) v.getTag();
        }

        LogManagement.i(TAG, String.valueOf(positon));
        AppInfo item = appInfos.get(positon);
        String packname = item.getPackname();


        // 判断当前列表的状态
        String titletext;
        TextView tv;
        if (v instanceof TextView) {
            tv = (TextView) v;
            titletext = tv.getText().toString();
            if ("所有程序".equals(titletext)) {
                item = appInfos.get(positon);
                packname = item.getPackname();
            } else {
                item = userAppInfos.get(positon);
                packname = item.getPackname();
            }
        } else {
            if ("所有程序".equals(app_manager_tv_title.getText().toString())) {
                item = appInfos.get(positon);
                packname = item.getPackname();
            } else {
                item = userAppInfos.get(positon);
                packname = item.getPackname();
            }
        }


        dismissPopWin();
        switch (v.getId()) {

            case R.id.ll_share:         //用户单击popupwindow的分享按钮的时候的代码
                LogManagement.i(TAG, "分享" + packname);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "推荐使用使用一款app" + item.getAppname());
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                AppManagerActivity.this.startActivity(shareIntent);
                break;



            case R.id.ll_uninstall:    //用户单击popupwindow的卸载按钮的时候的代码
                // 需求不能卸载系统的应用程序
                if (item.isSystemApp()) {
                    Toast.makeText(this, "系统应用不能被删除", Toast.LENGTH_SHORT).show();
                } else {
                    LogManagement.i(TAG, "卸载" + packname);
                    String uristr = new String("package:" + packname);
                    Intent deleteIntent = new Intent();
                    Uri uri = Uri.parse(uristr);
                    deleteIntent.setAction(Intent.ACTION_DELETE);
                    deleteIntent.setData(uri);
                    AppManagerActivity.this.startActivityForResult(deleteIntent, 0);
                }
                break;



            case R.id.ll_start:         //用户单击popupwindow的运行按钮的时候的代码
                LogManagement.i(TAG, "运行" + packname);
                // getPackageManager().queryIntentActivities(intent, flags);
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(
                            packname,
                            PackageManager.GET_UNINSTALLED_PACKAGES
                                    | PackageManager.GET_ACTIVITIES);       //得到所有package的信息
                    ActivityInfo[] activityinfos = info.activities;
                    if (activityinfos.length > 0) {       //只有有activity的信息的时候才执行
                        ActivityInfo startActivity = activityinfos[0];
                        Intent intent = new Intent();
                        intent.setClassName(packname, startActivity.name);    //设置启动的activity信息
                        startActivity(intent);                       //启动activity
                    } else {
                        Toast.makeText(this, "当前应用程序无法启动", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "应用程序无法启动", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;


            /**
             * 点击所以应用程序的时候响应事件
             */
            case R.id.app_manager_tv_ll_title:
                String allappTitle = "所有程序";//AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text);
                String title = app_manager_tv_title.getText().toString().trim();
                if(allappTitle.equals(title)){
                    app_manager_tv_title.setText(AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_user_text));
                    //app_manager_tv_title.setText("用户程序");
                    AppManagerActivity.this.initUI(false);


                }else{
                    app_manager_tv_title.setText(AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text));
                    //app_manager_tv_title.setText("用户程序");
                    AppManagerActivity.this.initUI(true);
                }
                break;
        }
    }




    /**
     *  启动一个Activity后的回调函数
     */
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        String allappTitle = AppManagerActivity.this.getResources().getString(R.string.app_manager_tv_title_text);
        String title = app_manager_tv_title.getText().toString().trim();
        if(allappTitle.equals(title)){
            AppManagerActivity.this.initUI(false);
        }else{
            AppManagerActivity.this.initUI(true);
        }

    }


    /**
     * 获得所有用户安装的app
     * @param appinfos  包含本手机中所有app的信息的List
     * @return   //仅包含用户app信息的List
     */
    private List<AppInfo> getUserApp(List<AppInfo> appinfos) {
        List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
        for(AppInfo appinfo:appInfos){
            if(!appinfo.isSystemApp()){
                userAppInfos.add(appinfo);
            }
        }
        return userAppInfos;
    }



    //cardlist
    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

    }
}
