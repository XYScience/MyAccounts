package com.science.myaccounts.common;

import android.content.Context;

import java.io.File;

/**
 * @author 幸运Science-陈土燊
 * @description 通常产量定义
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/18
 */

public class CommonDefine {

    public static final boolean IS_DEBUG = true;
    public static final boolean LOG_TO_FILE = true;
    public static String PATH_LOG;
    public static String PATH_ROOT;

    public static void initPath(Context context) {
        File externalFiles = context.getExternalFilesDir(null);
        if (externalFiles != null) {
            PATH_ROOT = externalFiles.getAbsolutePath();
        } else {
            PATH_ROOT = context.getFilesDir().getAbsolutePath();
        }

        PATH_LOG = PATH_ROOT + File.separator + "Log";

        initPath(CommonDefine.PATH_ROOT);
    }

    public static boolean initPath(String path) {
        File rootFile = new File(path);
        return initPath(rootFile);
    }

    public static boolean initPath(File rootFile) {
        return rootFile.exists() || rootFile.mkdirs();
    }
}
