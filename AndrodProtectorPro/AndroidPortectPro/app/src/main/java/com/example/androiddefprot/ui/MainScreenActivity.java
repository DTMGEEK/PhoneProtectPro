package com.example.androiddefprot.ui;


import com.example.androiddefprot.R;
import com.example.androiddefprot.adapter.MainUiAdapter;
import com.example.androiddefprot.custom.controls.MyImageView;
import com.example.androiddefprot.logmanagement.LogManagement;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @Title: MainScreenActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 主界面Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class MainScreenActivity extends Activity implements View.OnClickListener,View.OnTouchListener{   //,OnItemClickListener, {

    private static final String TAG = "MainScreenActivity";
    private GridView gv_main = null;
    private MainUiAdapter mainuiadapter = null;
    private SharedPreferences.Editor editor = null;
    private boolean isOpen = true;

    private MyImageView iv_mainscreen_stolen = null,iv_mainscreen_tel = null,
            iv_mainscreen_spftware_mag = null,iv_mainscreen_proce_manage = null;


    private List<ImageView> imageViewList = new ArrayList<ImageView>();
    private int[] imageViewRes = new int[]{
        R.id.attri_btn1_iv,
        R.id.iv_mainscreen_flow_mag,
        R.id.iv_mainscreen_antivirus,
        R.id.iv_mainscreen_tools,
        R.id.iv_mainscreen_system_optimize,
        R.id.iv_mainscreen_setting
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);
        initView();
        //this.gv_main = (GridView) this.findViewById(R.id.gv_main);
        this.mainuiadapter = new MainUiAdapter(this);
//        this.gv_main.setAdapter(mainuiadapter);
//        this.gv_main.setOnItemClickListener(this);

        this.editor = this.getSharedPreferences("config", Context.MODE_PRIVATE).edit();

   /*     this.gv_main.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view,
                                           int position, long id) {
                final EditText et = new EditText(MainScreenActivity.this);
                et.setHint("请输入文本");
                Dialog dialog = new AlertDialog.Builder(MainScreenActivity.this).setTitle("设置")
                        .setMessage("请输入要更新的名字").setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = et.getText().toString().trim();
                                if("".equals(name)){
                                    Toast.makeText(MainScreenActivity.this, "修改的名字不能为空", Toast.LENGTH_SHORT).show();
                                }else{
                                    MainScreenActivity.this.editor.putString("lostname", name);
                                    MainScreenActivity.this.editor.commit();
                                    TextView tv = (TextView) view.findViewById(R.id.tv_mainscreen);
                                    tv.setText(name);
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .create();
                dialog.show();
                return false;
            }
        });*/
    }


    /**
     * 初始界面
     */
    public void initView(){
      for(int i = 0; i < imageViewRes.length; i++){
          ImageView imageView = (ImageView) this.findViewById(imageViewRes[i]);
          imageView.setOnClickListener(this);
          imageViewList.add(imageView);
      }
        iv_mainscreen_stolen = (MyImageView) this.findViewById(R.id.iv_mainscreen_stolen);
        iv_mainscreen_tel = (MyImageView) this.findViewById(R.id.iv_mainscreen_tel);
        iv_mainscreen_spftware_mag = (MyImageView) this.findViewById(R.id.iv_mainscreen_spftware_mag);
        iv_mainscreen_proce_manage = (MyImageView) this.findViewById(R.id.iv_mainscreen_proce_manage);

        iv_mainscreen_stolen.setOnClickListener(this);
        iv_mainscreen_tel.setOnClickListener(this);
        iv_mainscreen_spftware_mag.setOnClickListener(this);
        iv_mainscreen_proce_manage.setOnClickListener(this);

        iv_mainscreen_stolen.setOnTouchListener(this);
        iv_mainscreen_tel.setOnTouchListener(this);
        iv_mainscreen_spftware_mag.setOnTouchListener(this);
        iv_mainscreen_proce_manage.setOnTouchListener(this);

    }



    /**
     * 当gridview的条目被点击的时候 对应的回调 parent :　girdview view : 当前被点击的条目 Linearlayout
     * position : 点击条目对应的位置 id : 代表的行号
     */
    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        LogManagement.i(this.TAG, "点击的位置" + position);
        switch(position){

            case 0: //手机防盗功能
                LogManagement.i(this.TAG, "进入手机防盗");
                Intent intent = new Intent(this,LostProtectedActivity.class);
                this.startActivity(intent);
                break;


            case 1: //进入通讯卫士
                LogManagement.i(this.TAG, "进入通讯卫士");
                Intent callsmsIntents = new Intent(this,CallSmsActivity.class);
                MainScreenActivity.this.startActivity(callsmsIntents);
                break;


            case 2://进入软件管理
                LogManagement.i(this.TAG, "进入软件管理");
                Intent appmanagerIntent = new Intent(this,AppManagerActivity.class);
                MainScreenActivity.this.startActivity(appmanagerIntent);
                break;


            case 3://进入任务管理
                LogManagement.i(this.TAG, "进入进程管理");
                Intent TaskIntent = new Intent(this,TaskManagerActivity.class);
                MainScreenActivity.this.startActivity(TaskIntent);
                break;

            case 4://进入流量管理
                LogManagement.i(this.TAG, "进入流量管理");
                Intent TrafficIntent = new Intent(this,TrafficManagerActivity.class);
                MainScreenActivity.this.startActivity(TrafficIntent);
                break;


            case 5://进入杀毒界面（特征杀毒）
                LogManagement.i(this.TAG, "进入杀毒");
                Intent AntiVirutsIntent = new Intent(this,AntiVirutsActivity.class);
                MainScreenActivity.this.startActivity(AntiVirutsIntent);
                break;


            case 6://进入高级工具
                LogManagement.i(this.TAG, "进入高级工具");
                Intent AToolsintent = new Intent(this,AToolsActivity.class);
                this.startActivity(AToolsintent);
                break;


            case 7://进入系统优化
                LogManagement.i(this.TAG, "进入系统优化");
                Intent Cacheintent = new Intent(this,CacheInfoActivity.class);
                this.startActivity(Cacheintent);
                break;


            case 8: //进入设置中心
                LogManagement.i(this.TAG, "进入设置中心");
                Intent Settingintent = new Intent(this,SettinCenterActivity.class);
                this.startActivity(Settingintent);
                break;
        }
    }*/


    /**
     * 当gridview的条目被点击的时候 对应的回调 parent :　girdview view : 当前被点击的条目 Linearlayout
     * position : 点击条目对应的位置 id : 代表的行号
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击下面的红点 激活动画
            case R.id.attri_btn1_iv:
                if(isOpen) {
                    startObjeAnim();  //开始动画
                 }else {
                    closeObjeAnim();  //结束动画
                }
            break;

            //点击手机防盗
            case R.id.iv_mainscreen_stolen:
                LogManagement.i(this.TAG, "进入手机防盗");
                Intent intent = new Intent(this,LostProtectedActivity.class);
                this.startActivity(intent);
            break;

            //进入通讯卫士
            case R.id.iv_mainscreen_tel:
                LogManagement.i(this.TAG, "进入通讯卫士");
                Intent callsmsIntents = new Intent(this,CallSmsActivity.class);
                MainScreenActivity.this.startActivity(callsmsIntents);
            break;


            case R.id.iv_mainscreen_spftware_mag:
                //进入软件管理
                LogManagement.i(this.TAG, "进入软件管理");
                Intent appmanagerIntent = new Intent(this,AppManagerActivity.class);
                MainScreenActivity.this.startActivity(appmanagerIntent);
            break;


            case R.id.iv_mainscreen_proce_manage:
                //进入进程管理
                LogManagement.i(this.TAG, "进入进程管理");
                Intent TaskIntent = new Intent(this,TaskManagerActivity.class);
                MainScreenActivity.this.startActivity(TaskIntent);
            break;

            case R.id.iv_mainscreen_flow_mag:
                //进入流量管理
                LogManagement.i(this.TAG, "进入流量管理");
                Intent TrafficIntent = new Intent(this,TrafficManagerActivity.class);
                MainScreenActivity.this.startActivity(TrafficIntent);
            break;

            case R.id.iv_mainscreen_antivirus:
                //进入杀毒界面（特征杀毒）
                LogManagement.i(this.TAG, "进入杀毒");
                Intent AntiVirutsIntent = new Intent(this,AntiVirutsActivity.class);
                MainScreenActivity.this.startActivity(AntiVirutsIntent);
            break;


            case R.id.iv_mainscreen_tools:
                //进入高级工具
                LogManagement.i(this.TAG, "进入高级工具");
                Intent AToolsintent = new Intent(this,AToolsActivity.class);
                this.startActivity(AToolsintent);
            break;


            case R.id.iv_mainscreen_system_optimize:
                //进入系统优化
                LogManagement.i(this.TAG, "进入系统优化");
                Intent Cacheintent = new Intent(this,CacheInfoActivity.class);
                this.startActivity(Cacheintent);
            break;


            case R.id.iv_mainscreen_setting:
                LogManagement.i(this.TAG, "进入设置中心");
                Intent Settingintent = new Intent(this,SettinCenterActivity.class);
                this.startActivity(Settingintent);
            break;

        }
    }


    /**
     * 展开菜单
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startObjeAnim() {

            for (int i = 1; i < imageViewRes.length; i++) {

                ObjectAnimator objectANimator2 = ObjectAnimator.ofFloat(imageViewList.get(i),
                        "translationX", 0.0f,  i*-80.0f);

               // AnimatorSet animatorSet = new AnimatorSet();
                //animatorSet.playTogether(objectAnimator,objectANimator2);
                objectANimator2.setInterpolator(new BounceInterpolator());
                //objectANimator2.setInterpolator(new AnticipateInterpolator());
               // objectANimator2.setInterpolator(new OvershootInterpolator());
                objectANimator2.setStartDelay(i * 300);
                objectANimator2.setDuration(500);
                objectANimator2.start();
            }
           isOpen = false;
     }


    /**
     * 关闭菜单
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void closeObjeAnim() {
        for(int i = 1; i < imageViewRes.length; i++){
            /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageViewList.get(i),
                    "translationY",i * (-150.0f),0.0f);*/
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(imageViewList.get(i),
                    "translationX",i * -80.0f,0.0f);
           // AnimatorSet animatorSet = new AnimatorSet();
           // objectAnimator2.playTogether(objectAnimator,objectAnimator2);
            objectAnimator2.setInterpolator(new OvershootInterpolator());
            objectAnimator2.setStartDelay(i*300);
            objectAnimator2.setDuration(500);
            objectAnimator2.start();
        }
        isOpen = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){
            //点击手机防盗
            case R.id.iv_mainscreen_stolen:
                Toast.makeText(this, "进入手机防盗",Toast.LENGTH_SHORT).show();
            break;

            //进入通讯卫士
            case R.id.iv_mainscreen_tel:
                Toast.makeText(this, "进入通讯卫士",Toast.LENGTH_SHORT).show();
            break;


            case R.id.iv_mainscreen_spftware_mag:
                //进入软件管理
                Toast.makeText(this, "进入软件管理",Toast.LENGTH_SHORT).show();
            break;


            case R.id.iv_mainscreen_proce_manage:
                //进入进程管理
                Toast.makeText(this, "进入进程管理",Toast.LENGTH_SHORT).show();
             break;

            case R.id.iv_mainscreen_flow_mag:
                //进入流量管理
                Toast.makeText(this, "进入流量管理",Toast.LENGTH_SHORT).show();
            break;

            case R.id.iv_mainscreen_antivirus:
                //进入杀毒界面（特征杀毒）
                Toast.makeText(this, "进入杀毒界面",Toast.LENGTH_SHORT).show();
            break;


            case R.id.iv_mainscreen_tools:
                //进入高级工具
                Toast.makeText(this, "进入高级工具",Toast.LENGTH_SHORT).show();
            break;


            case R.id.iv_mainscreen_system_optimize:
                //进入系统优化
                Toast.makeText(this, "进入系统优化",Toast.LENGTH_SHORT).show();
             break;


            case R.id.iv_mainscreen_setting:
                Toast.makeText(this, "进入设置中心",Toast.LENGTH_SHORT).show();
            break;
        }

        return false;
    }
}
