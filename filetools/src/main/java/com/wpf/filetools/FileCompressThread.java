package com.wpf.filetools;

import android.os.AsyncTask;

import com.wpf.filetools.Extract.Compress;

import java.io.File;
import java.io.IOException;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 文件压缩
 */

public abstract class FileCompressThread extends AsyncTask<File,Integer,Boolean> {

    private String zipFilePath,zipFileName;

    public FileCompressThread(File[] fileList,String zipFileName) {
        if(fileList != null && fileList.length>0) {
            this.zipFilePath = fileList[0].getPath();
            this.zipFileName = zipFileName;
            execute(fileList);
        }
    }

    public FileCompressThread(File[] fileList,String zipFilePath,String zipFileName) {
        if(fileList != null && fileList.length>0) {
            this.zipFilePath = zipFilePath;
            this.zipFileName = zipFileName;
            execute(fileList);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onFinish(aBoolean);
    }

    @Override
    protected Boolean doInBackground(File... files) {
        return compress(files);
    }

    private boolean compress(File... fileList) {
        try {
            return Compress.compress(fileList,zipFilePath,zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract void onFinish(boolean success);
}
