package com.linc.linceye.net.rx;

import java.io.IOException;
import java.lang.ref.WeakReference;

import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public abstract class RxSuperBaseModel<T> {

    private static final String UNKNOWN_MSG = "网络异常，请稍后再试";
    private static final String SEVER_ERROR_MSG = "服务器开小差";

    private WeakReference<SuperBaseModel> mSuperBaseModel;

    public RxSuperBaseModel(SuperBaseModel superBaseModel){
        this.mSuperBaseModel = new WeakReference<>(superBaseModel);
    }

    protected void checkThrowable(Throwable e){
        e.printStackTrace();

        if (e instanceof HttpException){
            ResponseBody body = ((HttpException) e).response().errorBody();
            int code = ((HttpException) e).code();

            //400-499 的 错误
            if (code >= 400 && code <= 499) {
                onError(code, SEVER_ERROR_MSG);
                return;
            }

            //500-599 的 错误
            if (code >= 500 && code <= 505) {
                onError(code, SEVER_ERROR_MSG);
                return;
            }

            //空响应体
            if (body == null) {
                onError(code, UNKNOWN_MSG);
                return;
            }

            try {
                onError(code, body.string());
            } catch (IOException e1) {
                onError(code, UNKNOWN_MSG);
            }
        } else {
            onError(-1, UNKNOWN_MSG);
        }
    }

    void addDisposable(Disposable d){
        SuperBaseModel superBaseModel = mSuperBaseModel.get();
        if (superBaseModel != null){
            superBaseModel.getDisposable().add(d);
        }
    }


    /**
     * 错误回调
     */
    protected abstract void onError(int code, String message);

    /**
     * 成功回调
     *
     * @param value
     */
    protected abstract void onSuccessRes(T value);
}
