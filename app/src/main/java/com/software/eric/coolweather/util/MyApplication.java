package com.software.eric.coolweather.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mzz on 2016/2/11.
 */
public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
