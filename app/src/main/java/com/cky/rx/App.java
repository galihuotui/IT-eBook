package com.cky.rx;

import android.app.Application;
import android.content.Context;

import com.cky.greendao.DaoMaster;
import com.cky.greendao.DaoSession;
import com.cky.rx.data.DaoManager;
import com.cky.rx.util.CrashHandler;

/**
 * Created by cuikangyuan on 16/5/7.
 */
public class App extends Application {
    private static App INSTANCE;

    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static DaoManager daoManager;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        daoManager = DaoManager.getInstance(getApplicationContext());
        CrashHandler crashHandler = CrashHandler.getsInstance();
        crashHandler.init(this);
    }

    //获取DaoMaster
    public static DaoMaster getDaoMaster(Context context) {
        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context, "itebook.db", null);
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        return daoMaster;
    }

    //获取DaoSession
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
