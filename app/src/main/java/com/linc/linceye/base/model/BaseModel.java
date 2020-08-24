package com.linc.linceye.base.model;

import android.os.Build;

import com.linc.base.mvvm.model.IBaseModelListener;
import com.linc.linceye.base.listener.IModelListener;
import com.linc.linceye.net.rx.SuperBaseModel;

import java.lang.ref.WeakReference;
/**
 * <通用的基类model> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/18
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseModel<T> extends SuperBaseModel<T> {

    /**
     * 数据加载成功
     *
     * @param data 数据
     * @param isEmpty 数据是否为空
     * @param isFirstPage 当前是否是第一页
     */
    public void loadSuccess(T data, boolean isEmpty, boolean isFirstPage)
    {
        synchronized (this)
        {
            mUiHandler.postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for (WeakReference<IBaseModelListener> weakListener : mWeakReferenceDeque)
                    {
                        if (weakListener.get() instanceof IModelListener)
                        {
                            IModelListener listenerItem =
                                    (IModelListener) weakListener.get();
                            if (null != listenerItem)
                            {
                                listenerItem.onLoadFinish(BaseModel.this,
                                        data);
                            }
                        }
                    }
                }
//                if (null != getCachekey() && isFirstPage)
//                {
//                    saveDataToLocal(data);
//                }
            }, 0);
        }
    }

    /**
     * @param prompt msg
     * @param isFirstPage 是否是第一页
     * */
    public void loadFail(String prompt, boolean isFirstPage)
    {
        synchronized (this)
        {
            mUiHandler.postDelayed(() -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    for (WeakReference<IBaseModelListener> weakListener : mWeakReferenceDeque)
                    {
                        if (weakListener.get() instanceof IModelListener)
                        {
                            IModelListener listenerItem =
                                    (IModelListener) weakListener.get();
                            if (null != listenerItem)
                            {
                                listenerItem.onLoadFail(BaseModel.this,
                                        prompt);
                            }
                        }
                    }
                }
            }, 0);
        }
    }
}
