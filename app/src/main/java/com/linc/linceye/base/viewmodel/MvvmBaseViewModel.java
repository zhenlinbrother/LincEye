package com.linc.linceye.base.viewmodel;

import androidx.lifecycle.ViewModel;

import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.linceye.net.rx.SuperBaseModel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class MvvmBaseViewModel<V, M extends SuperBaseModel> extends ViewModel implements IMvvmBaseViewModel<V> {
    private Reference<V> mUiRef;

    protected M model;
    @Override
    public void attachUi(V view) {
        mUiRef = new WeakReference<>(view);
    }

    @Override
    public V getPageView() {
        if (mUiRef == null){
            return null;
        }
        if (mUiRef.get() != null){
            return mUiRef.get();
        }
        return null;
    }

    @Override
    public boolean isUiAttach() {
        return mUiRef != null && mUiRef.get() != null;
    }

    @Override
    public void detachUi() {
        if (mUiRef != null){
            mUiRef.clear();
            mUiRef = null;
        }

        if (model != null){
            model.cancel();
        }
    }

    public abstract void initModel();
}
