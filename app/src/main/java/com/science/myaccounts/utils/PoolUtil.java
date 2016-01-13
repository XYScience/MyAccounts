package com.science.myaccounts.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 线程池
 */
public class PoolUtil {
    private static PoolUtil mPoolUtil = null;
    private ExecutorService mThreadPool;
    private PoolUtil(){
        mThreadPool = Executors.newCachedThreadPool();
    }

    public static synchronized PoolUtil getInstance(){
        if(mPoolUtil == null){
            mPoolUtil = new PoolUtil();
        }
        return mPoolUtil;
    }

    public Future submitTask(Runnable task) {
        return mThreadPool.submit(task);
    }

}
