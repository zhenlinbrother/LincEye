package com.linc.linceye.mvvm.home.nominate;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateAdapter;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.BannerCardViewModel;

import java.util.ArrayList;

public class NominateFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, NominateViewModel, BaseCustomViewModel>
        implements INominateView{

    private NominateAdapter mAdapter;


    public static NominateFragment newInstance(){
        NominateFragment fragment = new NominateFragment();
        return fragment;
    }
    @Override
    protected RecyclerView.Adapter getAdapter() {
        mAdapter =  new NominateAdapter(getContext(), mData);
        return mAdapter;
    }

    @Override
    protected void initView() { }

    @Override
    protected NominateViewModel getViewModel() {
        return new ViewModelProvider(this).get(NominateViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    public void onDataLoadFinish(ArrayList<BaseCustomViewModel> viewModels, boolean isFirstPage) {
        if (isFirstPage){
            mData.add(new BannerCardViewModel());
            mData.addAll(viewModels);
        } else {
            mData.addAll(viewModels);
        }
        mAdapter.setData(mData);
        mBaseAdapter.setLoadComplete();
    }

    @Override
    public void onLoadMoreFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setNoMore();
    }
}
