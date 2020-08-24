package com.linc.linceye.net.rx;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class RxObservableSubscribe<T> extends RxSuperBaseModel<T> implements Observer<T> {
    public RxObservableSubscribe(SuperBaseModel superBaseModel) {
        super(superBaseModel);
    }


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        addDisposable(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        onSuccessRes(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        checkThrowable(e);
    }

    @Override
    public void onComplete() {

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
