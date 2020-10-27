package com.linc.base.mvvm.activity;

import androidx.databinding.ViewDataBinding;

import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <通用> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/20
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class SimpleLoadMoreActivity<V extends ViewDataBinding, VM extends IMvvmBaseViewModel, DATA>
        extends BaseListMvvmActivity<V, VM> {

    protected final List<DATA> mData = new ArrayList<>();

    @Override
    public void getFirstData() {
        loadData(true);
    }

    @Override
    protected void loadMoreData() {
        loadData(false);
    }


    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void initData() {
    }

    protected abstract void loadData(boolean isFirst);

    public void onHandleResponseData(List<DATA> dataList, boolean isFirst){

        if (isFirst){
            mData.clear();

            mBaseAdapter.onSuccess();
        }

        if (dataList == null){
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

        if (dataList != null){
            mData.addAll(dataList);
        }

        mBaseAdapter.notifyDataSetChanged();
    }
}
