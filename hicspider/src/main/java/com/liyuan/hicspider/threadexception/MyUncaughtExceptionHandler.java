package com.liyuan.hicspider.threadexception;

/**
 * Created by weiyuan on 2019/8/7/007.
 */
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught    "+e);
    }
}
