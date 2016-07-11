package com.wpf.filetools.Util;

import java.util.Date;

/**
 * Created by wazsj on 6-26-0026.
 * 文件信息
 */

public class FileInfo {

    //是否是文件夹
    public boolean isDirectory;

    //文件夹下文件个数
    public int fileNum;

    //文件类型
    public String fileImageType;

    //文件名称
    public String fileName;

    //文件路径
    public String filePath;

    //上次修改时间
    public Date fileLastTime;

    //文件大小
    public long fileSize;

    public boolean isLongClick;

}
