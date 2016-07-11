package com.wpf.filetools.Extract;

import com.wpf.filetools.Util.CheckFile;
import com.wpf.filetools.Util.WriteFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 解压文件
 */

public class Extract {

    public static boolean Extract(String filePath,File zipFile) throws IOException {
        if(!zipFile.canRead()) return false;
        if(zipFile.isDirectory()) return false;
        ZipFile zFile = new ZipFile(zipFile);
        Enumeration zList = zFile.entries();
        while (zList.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry)zList.nextElement();
            if(zipEntry.isDirectory())
                CheckFile.CheckDir(filePath + zipEntry.getName());
            else {
                InputStream is = zFile.getInputStream(zipEntry);
                WriteFile.Write(is,filePath, zipEntry.getName());
            }
        }
        zFile.close();
        return true;
    }
}
