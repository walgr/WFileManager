package com.wpf.filetools;

import android.os.AsyncTask;

import com.wpf.filetools.Util.Config;
import com.wpf.filetools.Util.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wpf.filetools.FileUtils.getFileNum;
import static com.wpf.filetools.FileUtils.getFileType;

/**
 * Created by wazsj on 6-25-0025.
 * 文件夹扫描线程
 */

public abstract class FileScanThread extends AsyncTask<String,Integer,Boolean> {

    private List<FileInfo> fileInfoList = new ArrayList<>();

    protected FileScanThread(String scanFilePath) {
        this.execute(scanFilePath);
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        if(s) onFinish(fileInfoList);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        scanFile(strings[0]);
        return true;
    }

    private void scanFile(String scanFilePath) {
        File rootFile = new File(scanFilePath);
        if(!rootFile.canRead()) return;
        if(rootFile.isFile()) return;
        List<File> files = FileUtils.getSortList(rootFile,
                Config.canScanHideFile,Config.preferentialDisplayFolder);
        for(File file : files) {
            FileInfo fileInfo = getFileInfo(file);
            fileInfoList.add(fileInfo);
            addItem(fileInfo);
        }
    }

    private FileInfo getFileInfo(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.isDirectory = file.isDirectory();
        fileInfo.fileImageType = getFileType(file);
        fileInfo.fileName = getFileName(file);
        fileInfo.filePath = file.getPath();
        fileInfo.fileLastTime = new Date(file.lastModified());
        if(file.isFile())
            fileInfo.fileSize = file.length();
        else
            fileInfo.fileNum = getFileNum(file.getPath(),Config.canScanHideFile);
        return fileInfo;
    }

    private String getFileName(File file) {
        return file.getName();
    }

    public abstract void addItem(FileInfo fileInfo);

    public abstract void onFinish(List<FileInfo> fileInfoList);
}
