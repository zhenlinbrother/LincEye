package com.linc.linceye.mvvm.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.linc.linceye.bean.RespData;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.net.rx.SuperBaseModel;

public class TestModel extends SuperBaseModel {

    public TestModel() { }

    public void load(){
//        RetrofitFactory
//                .getApiService()
//                .getData()
//                .compose(RxParser.handleSingleDataResult())
//                .subscribe(new RxSingleSubscriber<Object>(mySelf) {
//
//                    @Override
//                    protected void onError(int code, String message) {
//
//                    }
//
//                    @Override
//                    protected void onSuccessRes(@NonNull Object value) {
//
//                    }
//                });
    }
}
