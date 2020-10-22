package com.linc.linceye;

import android.content.Context;
import android.os.Trace;
import android.util.Log;

import androidx.core.os.TraceCompat;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class MyApplication extends MultiDexApplication {

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
