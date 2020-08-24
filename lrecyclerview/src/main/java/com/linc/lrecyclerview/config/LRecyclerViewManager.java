package com.linc.lrecyclerview.config;

import com.linc.lrecyclerview.view.base.IBaseLoadMoreView;
import com.linc.lrecyclerview.view.base.IBaseRefreshLoadView;

public class LRecyclerViewManager {

    private static final LRecyclerViewManager INSTANCE = new LRecyclerViewManager();
    private IBaseRefreshLoadView mRefreshLoadView;
    private IBaseLoadMoreView mLoadMoreView;
    private boolean mIsDebug = false;

    public static LRecyclerViewManager getInstance(){ return INSTANCE; }

    public IBaseRefreshLoadView getRefreshLoadView() {
        return mRefreshLoadView;
    }

    public LRecyclerViewManager setRefreshLoadView(IBaseRefreshLoadView mRefreshLoadView) {
        this.mRefreshLoadView = mRefreshLoadView;
        return this;
    }

    public IBaseLoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public LRecyclerViewManager setLoadMoreView(IBaseLoadMoreView mLoadMoreView) {
        this.mLoadMoreView = mLoadMoreView;
        return this;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public LRecyclerViewManager setDebug(boolean mIsDebug) {
        this.mIsDebug = mIsDebug;
        return this;
    }
}

