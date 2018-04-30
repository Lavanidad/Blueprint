package com.deepspring.blueprint.base;


import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ZXingLibrary.initDisplayOpinion(this);
        Bmob.initialize(this, "d397d38814344ac1274230b357adde67");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    //内存清理的时候
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
