package com.linc.base.mvvm.fragment;

import androidx.databinding.ViewDataBinding;

import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleLoadMoreLazyFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel, DATA>
        extends BaseLazyListMvvmFragment<V, VM>{

    protected final List<DATA> mData = new ArrayList<>();

    @Override
    protected void loadMoreData() {
        loadData(false);
    }

    @Override
    protected void getFirstData() {
        loadData(true);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
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
