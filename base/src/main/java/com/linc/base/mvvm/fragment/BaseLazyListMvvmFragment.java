package com.linc.base.mvvm.fragment;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.R;
import com.linc.base.base.adapter.BitFrameAdapter;
import com.linc.base.base.listener.IStateListener;
import com.linc.base.base.manager.BitManager;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;

import com.linc.lrecyclerview.adapter.LRefreshAndLoadMoreAdapter;
import com.linc.lrecyclerview.recyclerview.LRecyclerView;

public abstract class BaseLazyListMvvmFragment<V extends ViewDataBinding, VM extends IMvvmBaseViewModel>
        extends BaseLazyMvvmFragment<V, VM> implements IStateListener {

    protected LRecyclerView mRecyclerView;
    protected BitFrameAdapter mBaseAdapter;

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        if (mBinding.getRoot() != null){
            mRecyclerView = mBinding.getRoot().findViewById(R.id.recycle_view);
        } else {
            mRecyclerView = rootView.findViewById(R.id.recycle_view);
        }

        this.initRecyclerView(this.mRecyclerView);
        this.setAdapter(this.getAdapter());
        initView();
        this.mBaseAdapter.onLoading();
        this.getFirstData();

    }

    private void setAdapter(RecyclerView.Adapter adapter) {
        this.mBaseAdapter = new BitFrameAdapter(getContext(), adapter);

        this.mBaseAdapter.setEmptyView(getEmptyView());
        this.mBaseAdapter.setLoadingView(getLoadingView());
        this.mBaseAdapter.setRetryView(getRetryView());
        this.mBaseAdapter.setRetryBtnId(getRetryBtnId());
        this.mBaseAdapter.setEmptyBtnId(getEmptyBtnId());

        this.mBaseAdapter.setIsOpenRefresh(requestRefresh());
        if (requestRefresh()) {
            this.mBaseAdapter.setOnRefreshListener(new LRefreshAndLoadMoreAdapter.OnRefreshListener() {
                @Override
                public void onRefreshing() {
                    BaseLazyListMvvmFragment.this.getFirstData();
                }
            });
        }

        this.mBaseAdapter.setIsOpenLoadMore(requestLoadMore());

        this.mBaseAdapter.setOnLoadMoreListener(new LRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoading() {
                loadMoreData();
            }
        });
        this.mBaseAdapter.setStateListener(this);

        this.initAdapterForChild(adapter);

        this.mRecyclerView.setAdapter(this.mBaseAdapter);
    }


    public   void initRecyclerView(LRecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.base_recycler_view;
    }

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract void initView();

    protected int getRetryView() {
        return BitManager.getInstance().getRetryViewLayout();
    }

    protected int getLoadingView() {
        return BitManager.getInstance().getLoadingViewLayout();
    }

    protected int getEmptyView() {
        return BitManager.getInstance().getEmptyViewLayout();
    }

    protected int getRetryBtnId() {
        return BitManager.getInstance().getRetryBtnId();
    }

    protected int getEmptyBtnId() {
        return BitManager.getInstance().getEmptyBtnId();
    }

    /**
     * 子View 可以加强 adapter 的功能
     *
     * @param adapter 用户传入的 Adapter
     */
    protected void initAdapterForChild(RecyclerView.Adapter adapter) {
    }

    /**
     * 是否需要下拉刷新功能
     *
     * @return true(默认)：需要刷新功能，false：不需要刷新功能
     */
    protected boolean requestRefresh() {
        return true;
    }

    @Override
    public void showContent() {
        mBaseAdapter.onSuccess();
    }

    @Override
    public void showLoading() {
        mBaseAdapter.onLoading();
    }

    @Override
    public void showEmpty() {
        mBaseAdapter.onEmpty();
    }

    @Override
    public void showFailure(String message) {
        mBaseAdapter.onError();
    }

    @Override
    public void onRetry() {
        getFirstData();
    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {
        getFirstData();
    }

    public void showLoadMoreEmpty(){
        mBaseAdapter.setLoadComplete();
    }

    protected abstract void loadMoreData();

    public boolean requestLoadMore(){
        return true;
    }
}
