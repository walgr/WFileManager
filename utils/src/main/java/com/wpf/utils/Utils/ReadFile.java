package com.wpf.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 王朋飞 on 11-26-0026.
 * 读取文件
 */

public class ReadFile {

    public static String readDataFormInputStream(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        BufferedReader bre = new BufferedReader(new InputStreamReader(inputStream));
        String str;
        try {
            while ((str = bre.readLine()) != null) {
                result.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result.toString();
    }
}
