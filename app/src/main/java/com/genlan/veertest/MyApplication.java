package com.genlan.veertest;

import android.app.Application;


/**
 * Description
 * Author Genlan
 * Date 2017/8/2
 */

public class MyApplication extends Application {

    private static MyApplication sInstance;

    public static MyApplication getInstance() {
        if (sInstance == null) {
            synchronized (MyApplication.class) {
                if (sInstance == null) {
                    throw new RuntimeException("Can't get Application Object!");
                }
            }
        }
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
