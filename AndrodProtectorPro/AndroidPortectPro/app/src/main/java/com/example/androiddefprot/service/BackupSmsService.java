package com.example.androiddefprot.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import com.example.androiddefprot.R;
import com.example.androiddefprot.domain.SmsInfo;
import com.example.androiddefprot.engine.SmsInfoService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

/**
 *
 * @Title: BackupSmsService.java
 * @Package com.example.androiddefprot.service
 * @Description: 短信备份服务类，在后台开启一个服务，再在服务里启动一个线程，备份信息。
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class BackupSmsService extends Service {

    private SmsInfoService smsinfoService = null;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        smsinfoService = new SmsInfoService(BackupSmsService.this);
        new Thread(){
            @Override
            public void run() {
                List<SmsInfo> infos = smsinfoService.getSmsInfos();
                try {
                    //File file = new File("/sdcard/smsbackup.xml");
                    File file = new File(getApplicationContext().getResources().getString(R.string.sms_backup_path)); //保存短信信息的文件
                    XmlSerializer xmlSerializer = Xml.newSerializer();   //xml序列化类
                    FileOutputStream os = new FileOutputStream(file);       //设置文件输出流

                    //把短信的信息和内容都写入到xml文件中
                    xmlSerializer.setOutput(os, "utf-8");
                    xmlSerializer.startDocument("utf-8", true);
                    xmlSerializer.startTag(null, "smss");
                    xmlSerializer.startTag(null, "count");
                    xmlSerializer.text(infos.size()+"");
                    xmlSerializer.endTag(null, "count");

                    for(SmsInfo info : infos){
                        xmlSerializer.startTag(null, "sms");

                        xmlSerializer.startTag(null, "id");
                        xmlSerializer.text(info.getId());
                        xmlSerializer.endTag(null, "id");



                        xmlSerializer.startTag(null, "address");
                        xmlSerializer.text(info.getAddress());
                        xmlSerializer.endTag(null, "address");


                        xmlSerializer.startTag(null, "date");
                        xmlSerializer.text(info.getDate());
                        xmlSerializer.endTag(null, "date");


                        xmlSerializer.startTag(null, "type");
                        xmlSerializer.text(info.getType()+"");
                        xmlSerializer.endTag(null, "type");



                        xmlSerializer.startTag(null, "body");
                        xmlSerializer.text(info.getBody());
                        xmlSerializer.endTag(null, "body");

                        xmlSerializer.endTag(null, "sms");
                    }
                    //把短信的信息和内容都写入到xml文件中
                    xmlSerializer.endTag(null, "smss");
                    xmlSerializer.endDocument();
                    //把文件缓冲区的数据写出去
                    os.flush();
                    os.close();
                    //把短信的信息和内容都写入到xml文件中

                    //因为这个一个服务，没有消息队列所以要收到创建一个消息队列
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "备份完成", Toast.LENGTH_LONG).show();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "备份失败", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }

        }.start();
    }



}
