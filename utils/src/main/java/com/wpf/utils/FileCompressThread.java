package com.wpf.utils;

import com.wpf.utils.Extract.Compress;

import java.io.File;
import java.io.IOException;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 文件压缩
 */

public abstract class FileCompressThread extends Thread {

    private File[] fileList;
    private String zipFilePath,zipFileName;

    public FileCompressThread(File[] fileList,String zipFileName) {
        this.fileList = fileList;
        if(fileList != null && fileList.length>0) this.zipFilePath = fileList[0].getPath();
        this.zipFileName = zipFileName;
    }

    public FileCompressThread(File[] fileList,String zipFilePath,String zipFileName) {
        this.fileList = fileList;
        this.zipFilePath = zipFilePath;
        this.zipFileName = zipFileName;
    }

    @Override
    public void run() {
        super.run();
        onFinish(compress());
    }

    private boolean compress() {
        try {
            return Compress.compress(fileList,zipFilePath,zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    abstract void onFinish(boolean success);
}
