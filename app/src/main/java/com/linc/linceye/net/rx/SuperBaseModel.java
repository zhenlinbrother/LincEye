package com.linc.linceye.net.rx;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.RequiresApi;

import com.linc.base.mvvm.model.IBaseModelListener;
import com.linc.base.mvvm.model.IModel;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class SuperBaseModel<T> implements IModel {

    private CompositeDisposable mDisposable;

    protected Handler mUiHandler = new Handler(Looper.getMainLooper());

    /**
     * 引用队列
     */
    protected ReferenceQueue<IBaseModelListener> mReferenceQueue;

    /**
     * 线程安全队列
     */
    protected ConcurrentLinkedDeque<WeakReference<IBaseModelListener>> mWeakReferenceDeque;

    protected SuperBaseModel mySelf;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SuperBaseModel() {
        this.mySelf = this;
        this.mDisposable = new CompositeDisposable();

        mReferenceQueue = new ReferenceQueue<>();
        mWeakReferenceDeque = new ConcurrentLinkedDeque<>();
    }

    public SuperBaseModel(SuperBaseModel mySelf) {
        this.mySelf = mySelf;
    }

    @Override
    public void onDestroy() {
//        if (mDisposable != null && !mDisposable.isDisposed()){
//            mDisposable.dispose();
//        }
//
//        mDisposable = null;
//        mySelf = null;
    }

    public CompositeDisposable getDisposable() {
        return mDisposable;
    }

    /**
     * 取消所有订阅
     */
    public void cancel()
    {
//        if (null != mDisposable && !mDisposable.isDisposed())
//        {
//            mDisposable.isDisposed();
//        }
    }

    /**
     * 对具体业务model进行注册区分
     *
     * @param listener 业务监听器
     */
    public void register(IBaseModelListener listener)
    {
        if (null == listener)
        {
            return;
        }
        synchronized (this)
        {
            // 每次注册的时候清理已经被系统回收的对象
            Reference<? extends IBaseModelListener> releaseListener = null;
            while ((releaseListener = mReferenceQueue.poll()) != null)
            {
                mWeakReferenceDeque.remove(releaseListener);
            }
            // 如果列队中 还存在此对象,就不用再次注册了
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (WeakReference<IBaseModelListener> weakListener : mWeakReferenceDeque)
                {
                    IBaseModelListener listenerItem = weakListener.get();
                    if (listenerItem == listener)
                    {
                        return;
                    }
                }
            }
            // 注册此listener对象
            WeakReference<IBaseModelListener> weaklistener =
                    new WeakReference<>(listener, mReferenceQueue);
            mWeakReferenceDeque.add(weaklistener);

        }
    }

    /**
     * 取消注册
     *
     * @param listener listener
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void unRegister(IBaseModelListener listener)
    {
        if (null == listener)
        {
            return;
        }
        synchronized (this)
        {
            for (WeakReference<IBaseModelListener> weakListener : mWeakReferenceDeque)
            {
                IBaseModelListener listenerItem = weakListener.get();
                if (listenerItem == listener)
                {
                    mWeakReferenceDeque.remove(weakListener);
                    break;
                }
            }
        }
    }

}
