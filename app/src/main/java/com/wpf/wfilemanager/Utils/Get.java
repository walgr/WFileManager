package com.wpf.wfilemanager.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.wpf.wfilemanager.R;

import java.text.DecimalFormat;

/**
 * Created by wazsj on 6-26-0026.
 * 获取相关
 */

public class Get {

    private static String[] sizeFormat = new String[] {"B","KB","MB","GB","TB"};

    public static Drawable getTypeImage(Context context, String fileType) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.folder);
        if("dir".equals(fileType))
            return drawable;
        if(fileType.isEmpty()) {
            drawable = context.getResources().getDrawable(R.mipmap.file_notype);
        } else {
            try {
                drawable = context.getResources().getDrawable(
                        context.getResources().getIdentifier(fileType, "mipmap", context.getPackageName()));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                drawable = context.getResources().getDrawable(R.mipmap.file_notype);
            }
        }
        return drawable == null ? context.getResources().getDrawable(R.mipmap.file_notype) : drawable;
    }

    public static String getFileSize(double size) {
        DecimalFormat df = new DecimalFormat("0.00");
        int i = 0;
        while (size>1024) { size/=1024; i++; }
        String strSize = String.valueOf(size);
        try {
            strSize = df.format(Double.parseDouble(strSize));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            strSize = "0";
        }
        String[] strings = strSize.split("\\.");
        if(strings.length >= 2) {
            if ("00".equals(strings[1]))
                strSize = strings[0];
        }
        strSize += sizeFormat[i];
        return strSize;
    }

}
