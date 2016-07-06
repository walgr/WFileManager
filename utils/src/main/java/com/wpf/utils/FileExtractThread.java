package com.wpf.utils;

import android.os.Environment;

import com.wpf.utils.Extract.Extract;
import com.wpf.utils.Utils.CheckFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 文件解压线程
 */

public abstract class FileExtractThread extends Thread {

    public static String defaultExtractPath = Environment.getExternalStorageDirectory().getPath() + "/DefaultExtract/";
    private String extractPath = "";
    private File zipFile;

    public FileExtractThread(File zipFile) {
        this.zipFile = zipFile;
        this.extractPath = defaultExtractPath;
    }

    public FileExtractThread(File zipFile,String extractPath) {
        this.zipFile = zipFile;
        this.extractPath = extractPath;
    }

    @Override
    public void run() {
        super.run();
        onFinish(extract());
    }

    private boolean extract() {
        try {
            return Extract.Extract(extractPath,zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    abstract void onFinish(boolean success);
}
