package com.linc.lrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linc.lrecyclerview.R;
import com.linc.lrecyclerview.view.base.IBaseLoadMoreView;
import com.linc.lrecyclerview.widget.BallSpinFadeLoader;


/**
 * <上拉刷新视图> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class OrdinaryLoadMoreView extends IBaseLoadMoreView {
    private View mLoadMoreView;
    private BallSpinFadeLoader mProgressBar;
    private TextView mTvTip;
    private ImageView mIvReload;

    public OrdinaryLoadMoreView(Context context) {
        this(context, (AttributeSet)null, 0);
    }

    public OrdinaryLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrdinaryLoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected View getLoadView() {
        return this.mLoadMoreView;
    }

    protected void onNoMore() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_no_more));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    protected void onError() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_reload));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(VISIBLE);
    }

    protected void onPullToAction() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_pull_to_load));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    protected void onReleaseToAction() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_release_to_load));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    protected void onExecuting() {
        this.mProgressBar.setVisibility(VISIBLE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_loading));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    protected void onDone() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(this.getContext().getString(R.string.l_recycler_loaded));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    protected View initView(Context context) {
        this.mLoadMoreView = LayoutInflater.from(context).inflate(R.layout.l_widget_oridinary_load_more_view, this, false);
        this.mProgressBar = (BallSpinFadeLoader)this.mLoadMoreView.findViewById(R.id.ball_loader);
        this.mTvTip = (TextView)this.mLoadMoreView.findViewById(R.id.tv_tip);
        this.mIvReload = (ImageView)this.mLoadMoreView.findViewById(R.id.iv_reload);
        return this.mLoadMoreView;
    }
}

