package com.science.myaccounts.utils;

import android.util.Log;

import com.science.myaccounts.common.CommonDefine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 写log
 */
public class Logger {
    private static final String DEFAULT_FLAG = "sssss";
    private static LogObserver mObserver;

    public static void i(String msg) {
        i(DEFAULT_FLAG, msg);
    }

    public static void i(String flag, String msg) {
        if (CommonDefine.IS_DEBUG) {
            Log.i(flag, msg);
            if (CommonDefine.LOG_TO_FILE) {
                writeToFile("i", flag, msg);
                if (mObserver != null) {
                    mObserver.onLogChange(msg);
                }
            }
        }
    }

    public static void w(String msg) {
        w(DEFAULT_FLAG, msg);
    }

    public static void w(String flag, String msg) {
        if (CommonDefine.IS_DEBUG) {
            Log.w(flag, msg);
            if (CommonDefine.LOG_TO_FILE) {
                writeToFile("w", flag, msg);
                if (mObserver != null) {
                    mObserver.onLogChange(msg);
                }
            }
        }
    }

    public static void e(String msg) {
        e(DEFAULT_FLAG, msg);
    }

    public static void e(String flag, String msg) {
        if (CommonDefine.IS_DEBUG) {
            Log.e(flag, msg);
            if (CommonDefine.LOG_TO_FILE) {
                writeToFile("e", flag, msg);
                if (mObserver != null) {
                    mObserver.onLogChange(msg);
                }
            }
        }
    }

    public static void logMatch(String msg) {
        logMatch(DEFAULT_FLAG, msg);
    }

    public static void logMatch(String flag, String msg) {
        if (CommonDefine.IS_DEBUG) {
            Log.i(flag, msg);
            if (CommonDefine.LOG_TO_FILE) {
                writeToFile("Match", flag, msg);
                if (mObserver != null) {
                    mObserver.onLogChange(msg);
                }
            }
        }
    }

    public static void writeToFile(final String level, final String flag, final String msg1) {
        PoolUtil.getInstance().submitTask(new Runnable() {
            @Override
            public void run() {
                String msg = CommonUtil.formatTime(System.currentTimeMillis()) + "  " + level + "   " + flag + "    " + msg1 + "\n";

                File logPath = new File(CommonDefine.PATH_LOG);
                if (!logPath.exists()) {
                    logPath.mkdirs();
                }

                File log = new File(logPath, "log.txt");
                FileWriter fw = null;
                try {
                    fw = new FileWriter(log, true);
                    fw.write(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void registerLogObserver(LogObserver observer) {
        mObserver = observer;
    }

    public static void unregisterLogObserver() {
        mObserver = null;
    }

    public static void clearLogFile() {
        File logPath = new File(CommonDefine.PATH_LOG);
        if (logPath.exists()) {
            File log = new File(logPath, "log.txt");
            if (log.exists()) {
                log.delete();
            }
        }
    }

    /**
     * 日志变化监听
     */
    public interface LogObserver {
        public void onLogChange(String msg);
    }
}
