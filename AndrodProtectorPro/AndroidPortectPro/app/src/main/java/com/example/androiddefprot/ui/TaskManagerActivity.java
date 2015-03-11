package com.example.androiddefprot.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.androiddefprot.MyApplication;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.TaskInfo;
import com.example.androiddefprot.engine.AppInfoProvider;
import com.example.androiddefprot.engine.TaskInfoProvider;
import com.example.androiddefprot.ui.stub.MyToast;
import com.example.androiddefprot.util.TextFormater;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



/**
 *
 * @Title: TaskManagerActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 线程管理Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TaskManagerActivity extends ActionBarActivity implements OnDismissCallback {

    private TextView task_manager_title_tv_count;
    private TextView task_manager_title_tv_availablememory;
    private ListView task_manager_lv;
    private LinearLayout task_manager_loading_ll;
    private ActivityManager am = null;
    private TaskInfoProvider taskInfoProvider = null;
    private List<RunningAppProcessInfo> runningappinfos;
    private List<TaskInfo> taskInfos = null;
    private TaskInfoAdapter adapter = null;
    private List<TaskInfo> userTaskInfos = null;
    private List<TaskInfo> systemTaskInfos = null;
    private View view = null;
    private int totalUsedMemorySize = 0;
    private Button task_manager_setting_bn = null;


    //多线程取得数据
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            task_manager_loading_ll.setVisibility(View.INVISIBLE);
            long totalmemoryinfo = totalUsedMemorySize * 1024 + TaskManagerActivity.this.getAvailableMemory();//计算可以使用的内存
            String strtotalmemory = TextFormater.getDataSize(totalmemoryinfo);
        /*    String str = TaskManagerActivity.this.task_manager_title_tv_availablememory
                    .getText().toString() + "  总内存：" + strtotalmemory;
            TaskManagerActivity.this.task_manager_title_tv_availablememory.setText(str);
            */
            adapter = new TaskInfoAdapter();

            //cardlist
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                    new SwipeDismissAdapter(adapter, TaskManagerActivity.this));
            //cardlist
            swingBottomInAnimationAdapter.setAbsListView(task_manager_lv);

            //cardlist
            task_manager_lv.setAdapter(swingBottomInAnimationAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // boolean flag = this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        //cardlist
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        //是否支持就可以自定义标题


        this.setContentView(R.layout.task_manager);
        this.taskInfos = new ArrayList<TaskInfo>();
        this.task_manager_lv = (ListView) this.findViewById(R.id.task_manager_lv);
        this.task_manager_loading_ll = (LinearLayout) this.findViewById(R.id.task_manager_loading_ll);
        this.task_manager_setting_bn = (Button) this.findViewById(R.id.task_manager_setting_bn);

       /* //
        if(flag){
            //把一个自定义的样式，设置给窗体
            this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.task_manager_title);
        }
        this.task_manager_title_tv_count = (TextView) this.findViewById(R.id.task_manager_title_tv_count);
        this.task_manager_title_tv_availablememory = (TextView) this.findViewById(R.id.task_manager_title_tv_availablememory);

*/
        this.fillData();

        //为ListView的每一个item设置点击监听
        task_manager_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Object obj = task_manager_lv.getItemAtPosition(position);

                if(obj instanceof TaskInfo){
                    TaskInfo taskinfo = (TaskInfo) obj;
                    CheckBox cb = (CheckBox) view.findViewById(R.id.task_manager_item_memory_cb);
                    String packagename = taskinfo.getPackagename();
                    if ("com.example.androiddefprot".equals(packagename)
                            || "android.process.media".equals(packagename)
                            || "system".equals(packagename)
                            || "android.process.acore".equals(packagename)){
                        cb.setVisibility(View.INVISIBLE);
                        return;
                    }
                    //因为checkbox会抢走焦点问题，所以手动更改CheckBox的状态
                    if(taskinfo.isCheck()){
                        taskinfo.setCheck(false);
                        cb.setChecked(false);
                    }else{
                        taskinfo.setCheck(true);
                        cb.setChecked(true);
                    }
                }
            }
        });


        //为ListView的每一个item设置长按监听
        TaskManagerActivity.this.task_manager_lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Intent detailIntent = new Intent(TaskManagerActivity.this,AppDetailActivity.class);
                MyApplication myApplication = (MyApplication) TaskManagerActivity.this.getApplication();
                Object obj = task_manager_lv.getItemAtPosition(position);
                if(obj instanceof TaskInfo){
                    myApplication.taskinfo = (TaskInfo) obj;
                    TaskManagerActivity.this.startActivity(detailIntent);
                }
                return false;
            }
        });

        /**
         * 为设置按钮设置监听
         */
        TaskManagerActivity.this.task_manager_setting_bn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManagerActivity.this.appSetting();
            }
        });
    }



    /**
     * 设置title的数据
     */
    /*public void setTitleData(){
        this.task_manager_title_tv_count.setText("进程数目:" + this.getProcessCount());
        this.task_manager_title_tv_availablememory.setText("剩余内存:"
                + TextFormater.getDataSize(this.getAvailableMemory()));
    }*/


    /**
     * 获取系统当前所有的进程数目
     * @return 当前进程的数量
     */
    public int getProcessCount(){
        runningappinfos = this.am.getRunningAppProcesses();
        return runningappinfos.size();
    }



    /**
     * 获取当前可用的系统剩余内存
     * @return 可以用的内存
     */
    public long getAvailableMemory(){
        MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        this.am.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }



    //
    public void fillData(){
       // TaskManagerActivity.this.setTitleData();
        TaskManagerActivity.this.task_manager_loading_ll.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                super.run();
                TaskInfoProvider taskInfoProvider = new TaskInfoProvider(TaskManagerActivity.this);
                TaskManagerActivity.this.taskInfos = taskInfoProvider
                        .getAllTasks(TaskManagerActivity.this.am
                                .getRunningAppProcesses());
                TaskManagerActivity.this.handler.sendEmptyMessage(0);
            }
        }.start();
    }




    /**
     * 线程信息数据适配器
     * @author jake
     *
     */
    private class TaskInfoAdapter extends BaseAdapter{

        public TaskInfoAdapter() {
            super();
            TaskManagerActivity.this.userTaskInfos = new ArrayList<TaskInfo>();
            TaskManagerActivity.this.systemTaskInfos = new ArrayList<TaskInfo>();
            for(TaskInfo info:taskInfos){
                if(info.isSystemapp()){
                    systemTaskInfos.add(info);
                }else{
                    userTaskInfos.add(info);
                }
            }
        }


        @Override
        public int getCount() {
            SharedPreferences sp = TaskManagerActivity.this
                    .getSharedPreferences("config", Context.MODE_PRIVATE);
            boolean showsystemapp = sp.getBoolean("showsystemapp", false);
            if(showsystemapp){
                return taskInfos.size() + 2;   //2 代表的是两个标签 用来显示程序的个数
            }else{
                return userTaskInfos.size() + 1;   //1 代表的是两个标签 用来显示程序的个数
            }
        }


        @Override
        public Object getItem(int position) {
            if(position == 0){
                return 0;
            }else if(position <= userTaskInfos.size()){
                return userTaskInfos.get(position-1);
            }else if(position == userTaskInfos.size()+1){
                return position;
            }else if(position <= taskInfos.size()+2){
                return systemTaskInfos.get(position - userTaskInfos.size() - 2);
            }else{
                return position;
            }
        }



        @Override
        public long getItemId(int position) {
            if(position == 0){
                return -1;
            }else if(position <= userTaskInfos.size()){
                return (position-1);
            }else if(position == userTaskInfos.size()+1){
                return -1;
            }else if(position <= userTaskInfos.size()+2){
                return  position - userTaskInfos.size()-2;
            }else{
                return -1;
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 把这些条目信息 做一下分类 系统进程和用户进程区分出来

            if (position == 0) {
                TextView tv_userapp = new TextView(TaskManagerActivity.this);
                tv_userapp.setTextColor(Color.BLACK);
                tv_userapp.setTextSize(16);
                tv_userapp.setText("用户进程 " + userTaskInfos.size() + "个");
                return tv_userapp;
            } else if (position <= userTaskInfos.size()) {
                int currentpositon = (position - 1);
                TaskInfo taskinfo = userTaskInfos.get(currentpositon);
                convertView = View.inflate(TaskManagerActivity.this,
                        R.layout.task_manager_item, null);
                ViewHolder holder = new ViewHolder();
                holder.task_manager_item_iv = (ImageView) convertView.findViewById(R.id.task_manager_item_iv);
                holder.task_manager_item_tv = (TextView) convertView.findViewById(R.id.task_manager_item_tv);
                holder.task_manager_memory_size_tv = (TextView) convertView
                        .findViewById(R.id.task_manager_item_memory_size_tv);
                holder.task_manager_memory_cb = (CheckBox) convertView
                        .findViewById(R.id.task_manager_item_memory_cb);
                if ("com.example.androiddefprot".equals(taskinfo
                        .getPackagename())
                        || "android.process.media".equals(taskinfo
                        .getPackagename())
                        || "system".equals(taskinfo.getPackagename())
                        || "android.process.acore".equals(taskinfo
                        .getPackagename())){
                    holder.task_manager_memory_cb.setVisibility(View.INVISIBLE);

                }
                holder.task_manager_item_iv.setImageDrawable(taskinfo.getAppicon());
                holder.task_manager_item_tv.setText(taskinfo.getAppname());
                holder.task_manager_memory_size_tv.setText("内存占用: "
                        + TextFormater.getKBDataSize(taskinfo.getMemorysize()));
                holder.task_manager_memory_cb.setChecked(taskinfo.isCheck());
                return convertView;

            } else if (position == userTaskInfos.size() + 1) {
                TextView tv_systemapp = new TextView(TaskManagerActivity.this);
                tv_systemapp.setTextSize(16);
                tv_systemapp.setTextColor(Color.BLACK);
                tv_systemapp.setText("系统进程 " + systemTaskInfos.size() + "个");

                return tv_systemapp;

            } else if (position <= systemTaskInfos.size() + 2) {
                int systemposition = (position - userTaskInfos.size() - 2);
                TaskInfo taskinfo = systemTaskInfos.get(systemposition);
                convertView = View.inflate(TaskManagerActivity.this,
                        R.layout.task_manager_item, null);
                ViewHolder holder = new ViewHolder();
                holder.task_manager_item_iv = (ImageView) convertView.findViewById(R.id.task_manager_item_iv);
                holder.task_manager_item_tv = (TextView) convertView.findViewById(R.id.task_manager_item_tv);
                holder.task_manager_memory_size_tv = (TextView) convertView
                        .findViewById(R.id.task_manager_item_memory_size_tv);
                holder.task_manager_memory_cb = (CheckBox) convertView
                        .findViewById(R.id.task_manager_item_memory_cb);

                if ("com.example.androiddefprot".equals(taskinfo
                        .getPackagename())
                        || "android.process.media".equals(taskinfo
                        .getPackagename())
                        || "system".equals(taskinfo.getPackagename())
                        || "android.process.acore".equals(taskinfo
                        .getPackagename())){
                    holder.task_manager_memory_cb.setVisibility(View.INVISIBLE);

                }
                holder.task_manager_item_iv.setImageDrawable(taskinfo.getAppicon());
                holder.task_manager_item_tv.setText(taskinfo.getAppname());
                holder.task_manager_memory_size_tv.setText("内存占用: "
                        + TextFormater.getKBDataSize(taskinfo.getMemorysize()));
                holder.task_manager_memory_cb.setChecked(taskinfo.isCheck());
                return convertView;

            } else {// 肯定不会执行
                return null;
            }

        }


    }
	

	/*View view = View.inflate(TaskManagerActivity.this, R.layout.task_manager_item, null);
	ViewHolder holder = new ViewHolder();
	holder.task_manager_item_iv = (ImageView) view.findViewById(R.id.task_manager_item_iv);
	holder.task_manager_item_tv = (TextView) view.findViewById(R.id.task_manager_item_tv);	
	holder.task_manager_memory_size_tv = (TextView) view.findViewById(R.id.task_manager_memory_size_tv);
	holder.task_manager_memory_cb = (CheckBox) view.findViewById(R.id.task_manager_memory_cb);
	
	
	int usedmemory = taskInfos.get(position).getMemorysize();
	
	holder.task_manager_item_iv.setImageDrawable(taskInfos.get(position).getAppicon());
	holder.task_manager_item_tv.setText(taskInfos.get(position).getAppname());
	holder.task_manager_memory_size_tv.setText(String.valueOf(TextFormater.getKBDataSize(usedmemory)));
	holder.task_manager_memory_cb.setChecked(taskInfos.get(position).isCheck());*/


    static class ViewHolder{
        public ImageView task_manager_item_iv;
        public TextView task_manager_item_tv;
        public TextView task_manager_memory_size_tv;
        public CheckBox task_manager_memory_cb;
    }


    /**
     * 进入进程管理器界面
     */
    public void appSetting(){
        Intent appSettingIntent = new Intent(TaskManagerActivity.this,TaskSettingActivity.class);
        TaskManagerActivity.this.startActivityForResult(appSettingIntent, 0);
    }



    /**
     * 通知ListView更新数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            TaskManagerActivity.this.fillData();
        }
    }



    /**
     * 杀死进程
     * @param view
     */
    public void killTask(View view){
        int total = 0;
        for(TaskInfo taskinfo:userTaskInfos){
            if(taskinfo.isCheck()){
                totalUsedMemorySize += taskinfo.getMemorysize();
                TaskManagerActivity.this.am.killBackgroundProcesses(taskinfo.getPackagename());
                total++;
            }
        }

        for(TaskInfo taskinfo:systemTaskInfos){
            if(taskinfo.isCheck()){
                totalUsedMemorySize += taskinfo.getMemorysize();
                TaskManagerActivity.this.am.killBackgroundProcesses(taskinfo.getPackagename());
                total++;
            }
        }

        String size = TextFormater.getKBDataSize(totalUsedMemorySize);
        //Toast.makeText(TaskManagerActivity.this, "一共杀死了"+total+"个进程"+"释放了"+size+"空间", Toast.LENGTH_LONG).show();
        MyToast.showToast(TaskManagerActivity.this, R.drawable.notification, "一共杀死了"+total+"个进程"+"释放了"+size+"空间");
        //通知ui更新
        adapter = new TaskInfoAdapter();
        task_manager_lv.setAdapter(adapter);
        this.fillData();
    }



    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

    }


}
