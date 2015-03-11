package com.example.androiddefprot.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检测是否链接网络
 * Created by jake（lian_weijian@126.com）
 * Time: 08:12
 */

public class NetUtils {

    private Context context = null;

    public NetUtils(Context context) {
        this.context = context;
    }

    public boolean isConnect2NetWork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;


       NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(null != info && info.isConnected()){
            isConnected = true;
        }
        return isConnected;
    }



    public void goToSetting(){
        Intent intent = new Intent();
        intent.setClassName("com.android.settings","com.android.settings.Settings");
        context.startActivity(intent);
    }

}
