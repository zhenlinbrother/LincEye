package com.linc.lrecyclerview.view.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.linc.lrecyclerview.utils.LogUtils;


/**
 * <刷新视图包装类> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class IBaseWrapperView extends LinearLayout {

    protected String TAG = this.getClass().getSimpleName();
    protected final static int INDEX = -1;
    private static final int SCROLL_DURATION = 300;
    public static final int STATE_ERROR = 1;
    public static final int STATE_PULL_TO_ACTION = 2;
    public static final int STATE_RELEASE_TO_ACTION = 4;
    public static final int STATE_EXECUTING = 8;
    public static final int STATE_DONE = 16;
    public static final int STATE_NO_MORE = 32;
    protected int mCurState;
    protected int mHeight;

    public IBaseWrapperView(Context context) {
        this(context, null, 0);
    }

    public IBaseWrapperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBaseWrapperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    private void init(Context context){
        this.mCurState = 2;
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.setMargins(0, 0, 0, 0);
        this.setLayoutParams(layoutParams);
        this.setPadding(0, 0, 0,0);
        View view = this.initView(context);
        this.wrapper(context, view);
    }

    public int getViewHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getCurState() {
        return this.mCurState;
    }

    public int getVisibleHeight(){
        LayoutParams layoutParams = (LayoutParams) this.getLoadView().getLayoutParams();
        return layoutParams.height;
    }

    protected void setVisibleHeight(int height){
        if (height <= 0){
            height = 0;
        }

        LayoutParams layoutParams = (LayoutParams) this.getLoadView().getLayoutParams();
        layoutParams.height = height;
        this.getLoadView().setLayoutParams(layoutParams);
    }

    public void setState(int state){
        if (state != this.mCurState){
            LogUtils.i("state:" + state);
            switch (state){
                case 2:
                    this.onPullToAction();
                    break;
                case 4:
                    this.onReleaseToAction();
                    break;
                case 8:
                    this.onExecuting();
                    break;
                case 16:
                    this.onDone();
                    break;
                default:
                    this.onOther(state);
                    break;
            }

            this.mCurState = state;
        }
    }

    protected void reset(int height){
        this.smoothScrollTo(height);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                IBaseWrapperView.this.setState(2);
            }
        }, 200L);
    }

    protected void smoothScrollTo(int height){
        this.smoothScrollTo(height, 300);
    }

    protected void smoothScrollTo(int height, int duration){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(this.getVisibleHeight(), height);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                IBaseWrapperView.this.setVisibleHeight((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    protected abstract View initView(Context context);

    protected abstract void wrapper(Context context, View view);

    protected abstract View getLoadView();

    protected abstract void onPullToAction();

    protected abstract void onReleaseToAction();

    protected abstract void onExecuting();

    protected abstract void onDone();

    protected abstract void onOther(int var1);
}
