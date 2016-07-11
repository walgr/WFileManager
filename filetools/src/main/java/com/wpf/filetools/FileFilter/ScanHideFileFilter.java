package com.wpf.filetools.FileFilter;


import java.io.File;
import java.io.FilenameFilter;

public class ScanHideFileFilter implements FilenameFilter {

    private boolean canScanHide;

    public ScanHideFileFilter(boolean canScanHide) {
        this.canScanHide = canScanHide;
    }

    @Override
    public boolean accept(File file, String s) {
        return canScanHide || !s.startsWith(".");
    }
}
