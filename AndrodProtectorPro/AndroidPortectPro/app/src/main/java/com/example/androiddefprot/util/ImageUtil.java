package com.example.androiddefprot.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Display;
import android.view.WindowManager;

/**
 *
 * @Title: ImageUtil.java
 * @Package com.example.androiddefprot.util
 * @Description: 图片工具类，可以返回一个规定大小的Bitmap对象
 * @author lian_weijian@163.com
 * @version V1.0
 */
public class ImageUtil {
    /**
     * 返回一个宽度和高度都为48个像素的bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap getResizedBitmap(BitmapDrawable drawable,
                                          Context context) {
        // drawable.
        // BitmapFactory.d
        Bitmap bitmap = drawable.getBitmap();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        if (height < 480 || width < 320) {
            return Bitmap.createScaledBitmap(bitmap, 32, 32, false);
        } else {
            return Bitmap.createScaledBitmap(bitmap, 48, 48, false);
        }
    }
}
