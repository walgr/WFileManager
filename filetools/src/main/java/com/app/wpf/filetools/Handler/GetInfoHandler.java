package com.app.wpf.filetools.Handler;

import android.os.Handler;
import android.os.Message;

import com.app.wpf.filetools.Util.Config;
import com.app.wpf.filetools.Util.FileInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static com.wpf.utils.FileUtils.getFileNum;
import static com.wpf.utils.FileUtils.getFileType;

/**
 * Created by wazsj on 6-25-0025.
 * 统一信息处理
 */

public abstract class GetInfoHandler extends Handler {

    private List<FileInfo> fileInfos = new ArrayList<>();

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0x01:      //  添加文件夹 文件
                File file = (File) msg.obj;
                FileInfo fileInfo = getFileInfo(file);
                fileInfos.add(fileInfo);
                getOneItem(fileInfo);
                break;
            case 0x02:

                break;
            case 0x03:

                break;

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

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public abstract void getOneItem(FileInfo fileInfo);
}
