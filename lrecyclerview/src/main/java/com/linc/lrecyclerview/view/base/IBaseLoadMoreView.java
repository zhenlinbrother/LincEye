package com.linc.lrecyclerview.view.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.linc.lrecyclerview.adapter.LRefreshAndLoadMoreAdapter;
import com.linc.lrecyclerview.utils.LogUtils;

public abstract class IBaseLoadMoreView extends IBaseWrapperView {
    protected LRefreshAndLoadMoreAdapter.OnLoadMoreListener mOnLoadMoreListener;

    public IBaseLoadMoreView(Context context) {
        super(context);
    }

    public IBaseLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IBaseLoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void wrapper(Context context, View view) {
        this.addView(view);
        this.measure(-1, -2);
        this.mHeight = this.getMeasuredHeight();
    }

    public void setOnLoadMoreListener(LRefreshAndLoadMoreAdapter.OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public LRefreshAndLoadMoreAdapter.OnLoadMoreListener getOnLoadMoreListener() {
        return this.mOnLoadMoreListener;
    }

    public boolean releaseAction(int visible) {
        boolean isOnRefresh = false;
        LogUtils.i("visible: " + visible + "; height: " + this.mHeight);
        if (visible > this.mHeight && this.mCurState < 8) {
            this.setState(8);
            isOnRefresh = true;
        }

        this.smoothScrollTo(this.mHeight);
        return isOnRefresh;
    }

    public void loadComplete() {
        this.setState(16);
        this.reset(this.mHeight);
    }

    public void reset() {
        this.setState(2);
        this.reset(this.mHeight);
    }

    public void loadError() {
        this.setState(1);
    }

    public void noMore() {
        this.setState(32);
        this.smoothScrollTo(this.mHeight);
    }

    protected void onOther(int state) {
        switch(state) {
            case 1:
                this.onError();
                break;
            case 32:
                this.onNoMore();
        }

    }

    public void onMove(int visibleHeight, float delta) {
        float viewHeight = (float)visibleHeight + delta;
        if (viewHeight < (float)this.mHeight) {
            viewHeight = (float)this.mHeight;
        }

        this.setVisibleHeight((int)viewHeight);
        LogUtils.i("visibleHeight: " + visibleHeight + "; " + "height: " + this.mHeight + "; " + "viewHeight: " + viewHeight);
        if (this.mCurState <= 4) {
            if (visibleHeight <= this.mHeight) {
                if (this.mCurState == 1) {
                    this.setState(1);
                } else {
                    this.setState(2);
                }
            } else {
                this.setState(4);
            }
        }

    }

    protected abstract void onNoMore();

    protected abstract void onError();
}

