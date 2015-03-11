package com.example.androiddefprot.ui;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.example.androiddefprot.R;
import com.example.androiddefprot.util.ImageUtil;
import com.example.androiddefprot.util.TextFormater;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @Title: TrafficManagerActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 流量管理Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TrafficManagerActivity extends Activity {

    private TextView traffic_manager_tv_total = null;
    private TextView traffic_manager_tv_wifi_total = null;
    private ImageView handle = null;
    private ListView content = null;
    private slidingDrawerAdapter adapter;
    private PackageManager pm = null;
    private Timer timer = null;
    private TimerTask task = null;

    //多线程通知列表更新信息
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setContentView(R.layout.traffic_manager);
        this.pm = this.getPackageManager();
        this.traffic_manager_tv_total = (TextView) this.findViewById(R.id.traffic_manager_tv_total);
        this.traffic_manager_tv_wifi_total = (TextView) this.findViewById(R.id.traffic_manager_tv_total);
        this.handle = (ImageView) this.findViewById(R.id.handle);
        this.content = (ListView) this.findViewById(R.id.content);
        content.addHeaderView(View.inflate(TrafficManagerActivity.this, R.layout.traffic_title, null));
        setTotalDataInfo();

        this.adapter = new slidingDrawerAdapter();
        System.out.print(this.adapter);
        this.content.setAdapter(adapter);

    }

    /**
     * 设置流量的信息
     */
    private void setTotalDataInfo() {
        //手机通过2g/3g的总流量
        long mobileRxBytes = TrafficStats.getMobileRxBytes();
        long mobileTxBytes = TrafficStats.getMobileTxBytes();
        long mobileTotal = mobileRxBytes + mobileTxBytes;

        String mobileTraffic = TextFormater.getDataSize(mobileTotal);
        traffic_manager_tv_total.setText(mobileTraffic);

        //获得手机的总流量
        long totalRx = TrafficStats.getTotalRxBytes();
        long totalTx = TrafficStats.getTotalTxBytes();
        long total = totalRx + totalTx;


        String wifiTraffic = TextFormater.getDataSize(total - mobileTotal);
        traffic_manager_tv_wifi_total.setText(wifiTraffic);

    }


    /**
     * slidingDrawer 数据适配器
     * @author jake
     *
     */
    private class slidingDrawerAdapter extends BaseAdapter{
        List<ResolveInfo> ResolveInfos = null;

        public slidingDrawerAdapter() {
            super();
            PackageManager pm = TrafficManagerActivity.this.getPackageManager();
            Intent packageIntent = new Intent();
            packageIntent.setAction("android.intent.action.MAIN");
            packageIntent.addCategory("android.intent.category.LAUNCHER");
            ResolveInfos = pm.queryIntentActivities(packageIntent, PackageManager.MATCH_DEFAULT_ONLY);
            System.out.print(ResolveInfos.size());
        }

        @Override
        public int getCount() {
            return ResolveInfos.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = new ViewHolder();
            //判断convertView是否可以重用，可以减少new的次数，方便回收
            if(convertView == null){
                view = View.inflate(getApplicationContext(), R.layout.traffic_item, null);
            }else{
                view = convertView;
            }
            holder.traffic_manager_icon = (ImageView) view.findViewById(R.id.traffic_item_icon);
            holder.traffic_item_tv_name = (TextView) view.findViewById(R.id.traffic_item_tv_name);
            holder.traffic_item_tv_upload = (TextView) view.findViewById(R.id.traffic_item_tv_upload);
            holder.traffic_item_tv_download = (TextView) view.findViewById(R.id.traffic_item_tv_download);
            ResolveInfo info = ResolveInfos.get(position);  //获得position位置的条目信息

            String appname = info.loadLabel(pm).toString(); //得到app的名字
            holder.traffic_item_tv_name.setText("     "+appname);



            BitmapDrawable appicon = (BitmapDrawable) info.loadIcon(pm); //得到app的图标
            Bitmap resizeicon = ImageUtil.getResizedBitmap((BitmapDrawable)appicon, TrafficManagerActivity.this);//转换更规定大小的图片
            holder.traffic_manager_icon.setImageBitmap(resizeicon);
            String packagename = info.activityInfo.packageName;      //得到包名
            PackageInfo packageinfo;
            try {
                packageinfo = pm.getPackageInfo(packagename, 0);     //
                int uid = packageinfo.applicationInfo.uid;

                long uidRxBytes = TrafficStats.getUidRxBytes(uid);  //得到某个uid即某个app的的上传流量信息
                long uidTxBytes = TrafficStats.getUidTxBytes(uid);  //得到某个uid即某个app的的下载流量信息
                if(uidTxBytes == -1){
                    uidTxBytes = 0;
                }
                if(uidRxBytes == -1){
                    uidRxBytes = 0;
                }

                holder.traffic_item_tv_upload.setText(TextFormater.getKBDataSize(uidTxBytes));
                holder.traffic_item_tv_download.setText(TextFormater.getKBDataSize(uidRxBytes));

            } catch (NameNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return view;
        }
    }



    private class ViewHolder{
        ImageView traffic_manager_icon = null;
        TextView traffic_item_tv_name = null;
        TextView traffic_item_tv_upload = null;
        TextView traffic_item_tv_download = null;
    }



    /**
     * 当界面给用户可见的时候，执行以下方法。
     */
    @Override
    protected void onStart() {
        super.onStart();
        TrafficManagerActivity.this.timer = new Timer();
        TrafficManagerActivity.this.task = new TimerTask(){

            @Override
            public void run() {
                Message msg = Message.obtain();
                handler.sendMessage(msg);
            }
        };
        this.timer.schedule(task, 1000, 2000);
    }


    /**
     *当界面给 用户不可见的时候，执行以下方法
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.timer.cancel();
        this.timer = null;
        this.task = null;
    }



}
