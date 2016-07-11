package com.wpf.wfilemanager.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 王朋飞 on 6-28-0028.
 * 配置文件存储
 */

public class ConfigSharePreference {

    private static String Flag_yc = "flag_yc";
    private static String Flag_yx = "flag_yx";

    public static boolean getBooleanYC(Context context) {
        return context.getSharedPreferences("Config",Context.MODE_PRIVATE).getBoolean(Flag_yc,false);
    }

    public static void setBooleanYC(Context context,boolean yc) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Config",Context.MODE_PRIVATE).edit();
        editor.putBoolean(Flag_yc,yc);
        editor.apply();
    }

    public static boolean getBooleanYX(Context context) {
        return context.getSharedPreferences("Config",Context.MODE_PRIVATE).getBoolean(Flag_yx,true);
    }

    public static void setBooleanYX(Context context,boolean yx) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Config",Context.MODE_PRIVATE).edit();
        editor.putBoolean(Flag_yx,yx);
        editor.apply();
    }
}
