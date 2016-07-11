package com.wpf.filetools;

import com.wpf.filetools.FileFilter.ScanHideFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    /** canScanHideFile 是否显示隐藏文件 */
    /** preferentialDisplayFolder 优先显示文件夹 */
    public static List<File> getSortList(File file, final boolean canScanHideFile, boolean preferentialDisplayFolder) {
        List<File> files = new ArrayList<>();
        if(preferentialDisplayFolder) {
            File[] dirList = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory()
                            && new ScanHideFileFilter(canScanHideFile).accept(file,file.getName());
                }
            });
            File[] fileList = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isFile() &&
                            new ScanHideFileFilter(canScanHideFile).accept(file,file.getName());
                }
            });
            List<File> dirArrayList = Arrays.asList(dirList);
            List<File> fileArrayList = Arrays.asList(fileList);
            Collections.sort(dirArrayList);
            Collections.sort(fileArrayList);
            files.addAll(dirArrayList);
            files.addAll(fileArrayList);
        } else {
            File[] fileList = file.listFiles(new ScanHideFileFilter(canScanHideFile));
            files = Arrays.asList(fileList);
            Collections.sort(files);
        }
        return files;
    }

    public static boolean addFile(String filePath,String fileName) {
        try {
            return new File(filePath + fileName).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addDir(String filePath) {
        return new File(filePath).mkdirs();
    }

    public static void delete(String filePath) {
        File file = new File(filePath);
        if(!file.canRead()) {
            return;
        }
        if(file.isFile()) {
            file.delete();
            return;
        }
        File[] files = file.listFiles();
        for(File f : files) delete(f.getPath());
        file.delete();
    }

    public static void delete(File file) {
        delete(file.getPath());
    }

    public static String getFileType(File file) {
        String type = "dir";
        if(file.isFile()) {
            String[] strings = file.getName().split("\\.");
            if(strings.length <= 1 || strings[0].isEmpty()) return "";
            type = strings[1];
        }
        return type;
    }

    public static int getFileNum(String filePath,boolean canScanHideFile) {
        File file = new File(filePath);
        if(!file.canRead()) return 0;
        if(file.list() == null) return 0;
        return file.list(new ScanHideFileFilter(canScanHideFile)).length;
    }

}
