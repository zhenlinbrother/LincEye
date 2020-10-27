package com.linc.linceye;

import android.content.Context;
import android.os.Trace;
import android.util.Log;

import androidx.core.os.TraceCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.linc.download.jerry.JerryDownloadConfig;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initDownload();
    }

    /**
     * 初始化下载
     */
    private void initDownload() {
        JerryDownloadConfig.setThreadCount(2);
        JerryDownloadConfig.setDownloadFolder("linc/download");
        JerryDownloadConfig.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        setRxJavaErrorHandler();
    }

    private void setRxJavaErrorHandler(){
        RxJavaPlugins.setErrorHandler(throwable -> {
            throwable.printStackTrace();
            Log.e("MyApplication", "MyApplication setRxJavaErrorHandler "  + throwable.getMessage());
        });
    }


}
