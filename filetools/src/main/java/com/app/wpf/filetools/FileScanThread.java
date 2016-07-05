package com.app.wpf.filetools;

import com.app.wpf.filetools.Handler.GetInfoHandler;
import com.app.wpf.filetools.Handler.SendMessage;
import com.app.wpf.filetools.Util.Config;
import com.app.wpf.filetools.Util.FileInfo;
import com.socks.library.KLog;
import com.wpf.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by wazsj on 6-25-0025.
 * 文件夹扫描线程
 */

public abstract class FileScanThread extends Thread {

    private String scanFilePath;

    private GetInfoHandler getInfoHandler = new GetInfoHandler() {
        @Override
        public void getOneItem(FileInfo fileInfo) {
            addItem(fileInfo);
        }
    };

    protected FileScanThread(String scanFilePath) {
        this.scanFilePath = scanFilePath;
    }

    @Override
    public void run() {
        super.run();
        scanFile();
        onFinish();
    }

    private void scanFile() {
        File rootFile = new File(scanFilePath);
        if(!rootFile.canRead()) {
            KLog.e("文件夹不可读.");
            return;
        }
        if(rootFile.isFile()) {
            KLog.e("非文件夹,请检查.");
            return;
        }
        List<File> files = FileUtils.getSortList(rootFile,
                Config.canScanHideFile,Config.preferentialDisplayFolder);
        for(File file : files) {
            SendMessage.send(getInfoHandler,0x01,file,0,0);
        }
    }

    protected List<FileInfo> getFileInfoList() {
        return getInfoHandler.getFileInfos();
    }

    public abstract void addItem(FileInfo fileInfo);

    public abstract void onFinish();
}
