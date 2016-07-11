package com.wpf.filetools;

import android.os.AsyncTask;

import com.wpf.filetools.Util.WriteFile;

import java.io.File;
import java.util.List;

/**
 * Created by 王朋飞 on 6-29-0029.
 * 文件拷贝线程
 */

public abstract class FileCoryThread extends AsyncTask<File,Integer,Boolean> {

    private String copyToPath = "";
    private boolean isClip;
    private boolean success = false;

    protected FileCoryThread(File[] copyFileList, String copyToPath, boolean isClip) {
        this.copyToPath = copyToPath;
        this.isClip = isClip;
        execute(copyFileList);
    }

    protected FileCoryThread(List<File> copyFileList, String copyToPath, boolean isClip) {
        this(copyFileList.toArray(new File[copyFileList.size()]),copyToPath,isClip);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        onFinish(aBoolean);
    }

    @Override
    protected Boolean doInBackground(File... files) {
        for(File file : files)
            copy(file);
        if(success && isClip) {
            for(File file : files) {
                String path = file.isFile()?
                        file.getPath().replace(file.getName(),""):file.getPath();
                if (!path.equals(copyToPath)) FileUtils.delete(file);
            }
        }
        return success;
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
