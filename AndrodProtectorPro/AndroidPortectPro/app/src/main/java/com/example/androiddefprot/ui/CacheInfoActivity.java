package com.example.androiddefprot.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.CacheInfo;
import com.example.androiddefprot.util.TextFormater;
import com.haarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 *
 * @Title: CacheInfoActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 获得App缓存信息的Activity
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class CacheInfoActivity extends ActionBarActivity implements OnDismissCallback {

    private ListView cache_info_lv = null;
    private PackageManager pm = null;
    private CacheInfoAdapter adapter = null;
    private Map<String,CacheInfo> maps = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setContentView(R.layout.cache_info_layout);
        this.maps = new HashMap<String,CacheInfo>();

        this.cache_info_lv = (ListView) this.findViewById(R.id.cache_info_lv);
        this.pm = this.getPackageManager();

        List<PackageInfo> infos = this.pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(PackageInfo info : infos){
            String name = info.applicationInfo.loadLabel(pm).toString();
            String packageName = info.packageName;
            CacheInfo cacheInfo = new CacheInfo();
            cacheInfo.setName(name);
            cacheInfo.setPackagename(packageName);
            maps.put(packageName, cacheInfo);


            this.getAppSize(packageName);
        }

        this.adapter = new CacheInfoAdapter();
        //cardList
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(
                new SwipeDismissAdapter(adapter, this));

        swingBottomInAnimationAdapter.setAbsListView(cache_info_lv);

        this.cache_info_lv.setAdapter(swingBottomInAnimationAdapter);
        //this.cache_info_lv.setAdapter(adapter);


        this.cache_info_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * Android2.3打开settings里面的那个应用的详细界面
                 * 后来我又查了一个Android4.1的，也是这样写的，所有应该是2.3之后，都是这样写的了，
                 * 但是这只是猜测，各位有空的可以去下载Android Settings的代码看一下
                 * 这样就可以做成多个版本的适配了
                 * <intent-filter>
                 *
                 * <category android:name="android.intent.category.DEFAULT">
                 * <data android:scheme="package">
                 * </data></category></action></intent-filter>
                 */

                /**
                 * Android2.2打开settings里面的那个应用的详细界面
                 * 用这个版本来打开的话，就要加多一句把包名设置进去的
                 * intent.putExtra("pkg", packageName);
                 * <intent-filter>
                 *
                 * <category android:name="android.intent.category.DEFAULT">
                 * <category android:name="android.intent.category.VOICE_LAUNCH">
                 * </category></category></action></intent-filter>
                 */

                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.addCategory("android.intent.category.DEFAULT");
                Set<String> set = maps.keySet();
                String[] pArray = new String[set.size()];
                set.toArray(pArray);
                intent.setData(Uri.parse("package:" + pArray[position]));
                startActivity(intent);
            }
        });

    }


    /**
     * 获得app的缓存信息
     * @param packageName
     * 根据包名获取应用程序的体积信息 注意: 这个方法是一个异步的方法 程序的体积要花一定时间才能获取到.
     *
     */
    private void getAppSize(final String packageName)  {
        try{
            Method method = PackageManager.class.getMethod("getPackageSizeInfo",
                    new Class[] { String.class, IPackageStatsObserver.class });

            // 调用 getPackageSizeInfo 方法，需要两个参数：1、需要检测的应用包名；2、回调
            method.invoke(this.pm, new Object[]{packageName,
                    new IPackageStatsObserver.Stub() {

                        @Override
                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                                throws RemoteException {
                            //Looper.prepare();
                            // 从pStats中提取各个所需数据
                            long cachesize = pStats.cacheSize;
                            long codesize = pStats.codeSize;
                            long datasize = pStats.dataSize;
                            CacheInfo cacheinfo = maps.get(packageName);
                            cacheinfo.setCache_size(TextFormater.getDataSize(cachesize));
                            cacheinfo.setCode_size(TextFormater.getDataSize(codesize));
                            cacheinfo.setData_size(TextFormater.getDataSize(datasize));
                            // 遍历一次消息队列，弹出Toast
                            // Looper.loop();
                        }
                    }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public void onDismiss(AbsListView listView, int[] reverseSortedPositions) {

    }


    /**
     * app数据缓存数据适配器
     * @author jake
     *
     */
    private class CacheInfoAdapter extends BaseAdapter{

        private Set<Entry<String,CacheInfo>> sets;
        private List<CacheInfo> cacheInfos;


        public CacheInfoAdapter() {
            super();
            sets = maps.entrySet();
            this.cacheInfos = new ArrayList<CacheInfo>();
            for(Entry<String,CacheInfo> entry :sets){
                cacheInfos.add(entry.getValue());
            }
        }

        @Override
        public int getCount() {
            return cacheInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return cacheInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            //优化
            if(convertView == null){
                view = View.inflate(CacheInfoActivity.this, R.layout.cache_info_item, null);
            }else{
                view = convertView;
            }
            TextView cache_info_item_tv_name = (TextView) view.findViewById(R.id.cache_info_item_tv_name);
            TextView cache_info_item_tv_code_size = (TextView) view.findViewById(R.id.cache_info_item_tv_code_size);
            TextView cache_info_item_tv_cache_size = (TextView) view.findViewById(R.id.cache_info_item_tv_cache_size);
            TextView cache_info_item_tv_data_size = (TextView) view.findViewById(R.id.cache_info_item_tv_data_size);
            CacheInfo cacheinfo = cacheInfos.get(position);
            cache_info_item_tv_name.setText(cacheinfo.getName());
            cache_info_item_tv_code_size.setText(cacheinfo.getCode_size());
            cache_info_item_tv_cache_size.setText(cacheinfo.getCache_size());
            cache_info_item_tv_data_size.setText(cacheinfo.getData_size());
            return view;
        }
    }


}
