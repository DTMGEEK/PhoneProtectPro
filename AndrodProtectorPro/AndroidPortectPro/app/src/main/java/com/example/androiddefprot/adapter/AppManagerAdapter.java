package com.example.androiddefprot.adapter;

import java.util.List;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.AppInfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 *
 * @Title: AppManagerAdapter.java
 * @Package com.example.androiddefprot.adapter
 * @Description: 应用程序列表Adapter
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class AppManagerAdapter extends BaseAdapter {
	
	private static final String TAG = "AppManagerAdapter";
	private Context context;
	private List<AppInfo> appinfos = null;
	private static ImageView iv = null; //事先声明一个静态变量，预先申请了内存空间。可以起到优化ListView的作用  空间换时间
	private static TextView tv = null;  //事先声明一个静态变量，预先申请了内存空间。可以起到优化ListView的作用   空间换时间


    //cardlist
    private LruCache<Integer, Bitmap> mMemoryCache;

	
	public AppManagerAdapter(Context context, List<AppInfo> appinfos) {
		super();
		this.context = context;
		this.appinfos = appinfos;


        //cardlist
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory;
        mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };


	}
	

	@Override
	public int getCount() {
		
		return appinfos.size();
	}

	@Override
	public Object getItem(int position) {
		return appinfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


    /**
     * View convertView (转化view对象 ,历史view对象的缓存) convertview 就是拖动的时候被回收掉的view对象
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appinfo = appinfos.get(position);
		View view = null;
        ViewHolder viewHolder= null;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.app_item, null);  //这段代码相当耗费时间
            viewHolder.textView = (TextView) convertView.findViewById(R.id.app_name_tv);
            viewHolder.imageView =  (ImageView) convertView.findViewById(R.id.app_name_iv);
            //LogManagement.i(TAG,"通过资源文件 创建view对象");
            convertView.setTag(viewHolder);
		}else{
            //LogManagement.i(TAG,"使用历史缓存view对象");
            viewHolder = (ViewHolder) convertView.getTag();
		}

        Bitmap bitmap = ((BitmapDrawable)appinfo.getIcon()).getBitmap();

       // viewHolder.imageView.setImageDrawable(appinfo.getIcon());
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.textView.setText(appinfo.getAppname());
		return convertView;
	}


    private void addBitmapToMemoryCache(int key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(int key) {
        return mMemoryCache.get(key);
    }


    private class ViewHolder{
        TextView textView;
        ImageView imageView;
    }

}
