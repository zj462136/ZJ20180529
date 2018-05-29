package com.bwie.zhangjun.application;

import android.app.Application;

import com.bwie.zhangjun.dao.DaoMaster;
import com.bwie.zhangjun.dao.DaoSession;
import com.facebook.drawee.backends.pipeline.Fresco;

public class MyApplication extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "baway1.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoSession(){
        return daoSession;
    }
}
