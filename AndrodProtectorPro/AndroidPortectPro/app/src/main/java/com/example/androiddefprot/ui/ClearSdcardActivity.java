package com.example.androiddefprot.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.example.androiddefprot.R;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 *
 * @Title: ClearSdcardActivity.java
 * @Package com.example.androiddefprot.ui
 * @Description: 清理sd卡Activity  通过查询数据库实现，
 * 通过查询数据库中比较常用的应用的缓存文件夹，如果查询到有就把对应的sd卡文件夹删除
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class ClearSdcardActivity extends Activity {

    private TextView clearsdcard_tv_title = null;
    private ProgressBar clearsdcard_pb = null;
    private Button clearsdcard_bn = null;
    private SQLiteDatabase db = null;
    private String db_name = null ;
    private String db_path = null;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.clearsdcard);
        this.clearsdcard_tv_title = (TextView) this.findViewById(R.id.clearsdcard_tv_title);
        this.clearsdcard_pb = (ProgressBar) this.findViewById(R.id.clearsdcard_pb);
        this.clearsdcard_bn = (Button) this.findViewById(R.id.clearsdcard_bn);
        this.db_name = this.getResources().getString(R.string.clearsdcard_db_name);
        this.db_path = this.getResources().getString(R.string.clearsdcard_db_path);
        copyFile();

    }




    //如果不存在常用号码数据库，就在apk中复制到sd卡中
    private void copyFile() {
        AssetManager am = this.getAssets();
        try {
            File file = new File(db_path);
            if(!file.exists()){
                InputStream is = am.open(db_name);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = is.read(buffer))!= -1){
                    fos.write(buffer, 0, len);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    //按钮点击事件
    public void start(View view){
        this.db = SQLiteDatabase.openDatabase(db_path, null, SQLiteDatabase.OPEN_READONLY);
        new Thread(){        //读取匹配文件会比较耗时，所以启动一个新的线程进行处理

            @Override
            public void run() {
                super.run();
                PackageManager pm = ClearSdcardActivity.this.getPackageManager();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                ClearSdcardActivity.this.clearsdcard_pb.setMax(infos.size());
                int total = 0;
                for(PackageInfo info: infos){
                    String packagename = info.packageName;
                    String sql = "select filepath from softdetail where apkname = ?";
                    Cursor cursor = ClearSdcardActivity.this.db.rawQuery(sql, new String[]{packagename});
                    if(cursor.moveToNext()){
                        String path = cursor.getString(0);
                        File delFile = new File(Environment.getExternalStorageDirectory(),path);
                        delDir(delFile);//删除文件的方法

                        try {
                            this.sleep(2000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        total++; //更改进度条进度
                        ClearSdcardActivity.this.clearsdcard_pb.setProgress(total);
                        cursor.close();
                        Message msg = Message.obtain(); //从消息队列中获取空闲的message，可以节省资源消耗
                        msg.obj = "清除" + packagename;
                        handler.sendMessage(msg);
                    }
                }
                Message msg = Message.obtain();
                msg.obj = "清理完成";
                handler.sendMessage(msg);
                db.close();
            }
        };
    }




    /**
     * 用递归实现文件的删除
     * @param delFile
     */
    private void delDir(File delFile) {
        if(delFile.isDirectory()){ //如果传热2的是一个文件夹，就递归，直到是文件为止
            File[] files = delFile.listFiles();
            for(int i = 0; i < files.length; i++){
                delDir(files[i]);
            }
        }else{
            delFile.delete();
        }
    }


}
