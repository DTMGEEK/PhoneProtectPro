package com.example.androiddefprot.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @Title: MD5Encoder.java
 * @Package com.example.androiddefprot.util
 * @Description: md5编码加密
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class MD5Encoder {

    public static String encode(String pwd){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(pwd.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < bytes.length; i++){
                String s = Integer.toHexString(0xff&bytes[i]);
                if(s.length() == 1){
                    sb.append("0"+s);
                }else{
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
}
