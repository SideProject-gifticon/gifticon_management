package com.example.gifticon_management;

import android.app.Application;

public class MyApp extends Application {
    private static MyApp instance;

    @Override
    public void onCreate() { // 앱 실행하면 제일 먼저 실행됨
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }
}
