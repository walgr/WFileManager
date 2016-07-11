package com.wpf.filetools.Extract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by 王朋飞 on 7-6-0006.
 * 压缩文件
 */

public class Compress {

    public static boolean compress(File[] fileList,String zipFilePath,String zipFileName) throws IOException {
        for(File file : fileList) {
            File zipFile = new File(zipFilePath + zipFileName);
            InputStream input = null;
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    input = new FileInputStream(file1);
                    zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + file1.getName()));
                    byte[] temp = new byte[1024];
                    int len = 0;
                    while ((len = input.read(temp)) != -1) {
                        zipOut.write(temp, 0, len);
                    }
                    input.close();
                }
            }
            zipOut.close();
        }
        return true;
    }
}
