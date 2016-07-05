package com.wpf.utils;

import com.wpf.utils.Utils.WriteFile;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 6-29-0029.
 * 文件拷贝线程
 */

public abstract class FileCoryThread extends Thread {

    private List<File> copyFileList = new ArrayList<>();
    private String copyToPath = "";
    private boolean isClip;
    private boolean success = false;

    protected FileCoryThread(List<File> copyFileList, String copyToPath, boolean isClip) {
        this.copyFileList = copyFileList;
        this.copyToPath = copyToPath;
        this.isClip = isClip;
    }

    @Override
    public void run() {
        super.run();
        for(File file : copyFileList)
            copy(file);
        if(success && isClip) {
            for(File file : copyFileList) {
                String path = file.isFile()?
                        file.getPath().replace(file.getName(),""):file.getPath();
                if (!path.equals(copyToPath)) FileUtils.delete(file);
            }
        }
        onFinish(success);
    }

    private void copy(File file) {
        if(!file.canRead())
            return;
        if(file.isFile()) success = WriteFile.Write(file,copyToPath);
        else {
            if (!file.exists()) file.mkdirs();
            File[] files = file.listFiles();
            for (File f : files) copy(f);
        }
    }

    public abstract void onFinish(boolean success);
}
