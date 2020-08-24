package com.linc.linceye.net.rx;

import androidx.annotation.NonNull;



import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class RxSingleSubscriber<T> extends RxSuperBaseModel<T> implements SingleObserver<T> {
    public RxSingleSubscriber(SuperBaseModel superBaseModel) {
        super(superBaseModel);
    }

    @Override
    public void onSubscribe(Disposable d) {
        addDisposable(d);
    }


    @Override
    public void onSuccess(T value) {
        onSuccessRes(value);
    }

    @Override
    public void onError(Throwable e) {
        checkThrowable(e);
    }

    protected abstract void onError(int code, String message);

    protected abstract void onSuccessRes(@NonNull T value);
}
