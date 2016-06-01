package com.cky.rx;

import android.app.Application;

import com.cky.rx.util.CrashHandler;

/**
 * Created by cuikangyuan on 16/5/7.
 */
public class App extends Application {
    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);
    }
}
