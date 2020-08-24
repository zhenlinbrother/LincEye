package com.linc.base.mvvm.adapter;

import android.content.Context;

import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;

import java.util.List;

/**
 * <简易MVVM 适配器> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class SimpleDataBindingAdapter<M, B extends ViewDataBinding> extends BaseDataBindingAdapter<M, B> {

    private final int layout;
    protected final List<M> mData;
    protected SimpleDataBindingAdapter(Context context, int layout, List<M> data) {
        super(context);
        this.layout = layout;
        mData = data;
        this.setItems(mData);
    }

    @Override
    protected @LayoutRes
    int getLayoutResId(int viewType) {
        return this.layout;
    }

}
