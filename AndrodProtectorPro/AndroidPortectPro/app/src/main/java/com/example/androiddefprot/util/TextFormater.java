package com.example.androiddefprot.util;

import java.text.DecimalFormat;

/**
 *
 * @Title: TextFormater.java
 * @Package com.example.androiddefprot.util
 * @Description: 返回byte的数据大小对应的文本
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class TextFormater {

    public static String getDataSize(long size){
        DecimalFormat formater = new DecimalFormat("####.00");
        if(size < 1024){
            return size + "bytes";
        }else if(size < 1024*1024){
            float kbsize = size/1024f;
            return formater.format(kbsize) + "KB";
        }else if(size < 1024*1024*1024){
            float mbsize = size/1024f/1024f;
            return formater.format(mbsize) + "MB";
        }else if(size < 1024*1024*1024*1024){
            float gbsize = size/1024f/1024f/1024f;
            return formater.format(gbsize) + "GB";
        }
        return "size,error";
    }



    public static String getKBDataSize(long size){
        return getDataSize(size*1024);
    }

}
