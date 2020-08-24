package com.linc.lrecyclerview.view.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.linc.lrecyclerview.adapter.LRefreshAndLoadMoreAdapter;
import com.linc.lrecyclerview.utils.LogUtils;


/**
 * <下拉刷新视图> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class IBaseRefreshLoadView extends IBaseWrapperView{

    private LRefreshAndLoadMoreAdapter.OnRefreshListener mOnRefreshListener;

    public IBaseRefreshLoadView(Context context) {
        this(context, null, 0);
    }

    public IBaseRefreshLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBaseRefreshLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void wrapper(Context context, View view){
        this.addView(view);
        this.measure(-1, -2);
        this.mHeight = this.getMeasuredHeight();
        this.setVisibleHeight(0);
    }

    public void setOnRefreshListener(LRefreshAndLoadMoreAdapter.OnRefreshListener listener) {
        this.mOnRefreshListener = listener;
    }

    public LRefreshAndLoadMoreAdapter.OnRefreshListener getOnRefreshListener() {
        return this.mOnRefreshListener;
    }

    public boolean releaseAction(int visibleHeight){
        boolean isRefresh = false;
        int destHeight = 0;
        if (this.mCurState == 8){
            destHeight = this.mHeight;
        }

        if (visibleHeight > this.mHeight && this.mCurState < 8){
            this.setState(8);
            destHeight = this.mHeight;
            isRefresh = true;
        }

        this.smoothScrollTo(destHeight);
        return isRefresh;
    }

    public void onMove(int visibleHeight, int delta){
        LogUtils.i("[visibleHeight: " + visibleHeight + ";" + "delta: " + delta + "]");
        if (visibleHeight > 0 || delta > 0){
            this.setVisibleHeight(visibleHeight + delta);
        }
        if (this.mCurState <= 4){
            if (visibleHeight <= this.mCurState){
                this.setState(2);
            } else {
                this.setState(4);
            }
        }

//        int height;
//        if (visibleHeight >= this.mHeight){
//            height = this.mHeight;
//        } else {
//            height = visibleHeight;
//        }
    }

    public void refreshComplete(){
        this.setState(16);
        this.reset(0);
    }

    protected void onOther(int state){

    }
}
