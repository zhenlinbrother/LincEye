package com.linc.linceye.base.listener;

import com.linc.base.mvvm.model.IBaseModelListener;
import com.linc.linceye.base.model.BaseModel;

public interface IModelListener<T> extends IBaseModelListener {

    /**
     * 数据加载成功
     * @param model
     * @param data
     */
    void onLoadFinish(BaseModel model, T data);

    /**
     * 数据加载失败
     * @param model
     * @param prompt
     */
    void onLoadFail(BaseModel model, String prompt);
}
