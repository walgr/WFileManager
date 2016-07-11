package com.wpf.filetools;

import android.os.AsyncTask;
import android.os.Environment;

import com.wpf.filetools.Extract.Extract;

import java.io.File;
import java.io.IOException;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 文件解压线程
 */

public abstract class FileExtractThread extends AsyncTask<File,Integer,Boolean> {

    private static String defaultExtractPath = Environment.getExternalStorageDirectory().getPath()
            + "/DefaultExtract/";
    private String extractPath = "";

    public FileExtractThread(File zipFile) {
        this(zipFile,defaultExtractPath);
    }

    public FileExtractThread(File zipFile,String extractPath) {
        this.extractPath = extractPath;
        execute(zipFile);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onFinish(aBoolean);
    }

    @Override
    protected Boolean doInBackground(File... files) {
        return extract(files[0]);
    }

    private boolean extract(File zipFile) {
        try {
            return Extract.Extract(extractPath,zipFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract void onFinish(boolean success);
}
