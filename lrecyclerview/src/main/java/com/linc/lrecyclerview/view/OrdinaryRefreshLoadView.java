package com.linc.lrecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.linc.lrecyclerview.R;
import com.linc.lrecyclerview.view.base.IBaseRefreshLoadView;
import com.linc.lrecyclerview.widget.BallSpinFadeLoader;


/**
 * <原始下拉刷新视图> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class OrdinaryRefreshLoadView extends IBaseRefreshLoadView {

    private View mLoadView;
    private static final int ANIM_DURATION = 180;
    private TextView mTvRefreshStatus;
    private ImageView mIvArrow;
    private BallSpinFadeLoader mBallLoader;
    private RotateAnimation mArrowToUpAnim;
    private RotateAnimation mArrowToDownAnim;

    public OrdinaryRefreshLoadView(Context context) {
        this(context, null, 0);
    }

    public OrdinaryRefreshLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrdinaryRefreshLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View initView(Context context) {
        this.mLoadView = LayoutInflater.from(context).inflate(R.layout.l_widget_oridinary_refresh_view, this, false);
        this.mTvRefreshStatus = this.mLoadView.findViewById(R.id.tv_refresh_status);
        this.mIvArrow = this.mLoadView.findViewById(R.id.iv_arrow);
        this.mBallLoader = this.mLoadView.findViewById(R.id.ball_loader);
        this.mArrowToUpAnim = new RotateAnimation(0.0F, 180.0F, 1, 0.5F, 1, 0.5F);
        this.mArrowToUpAnim.setDuration(180L);
        this.mArrowToUpAnim.setFillAfter(true);
        this.mArrowToDownAnim = new RotateAnimation(180.0F, 0.0F, 1, 0.5F, 1, 0.5F);
        this.mArrowToDownAnim.setDuration(180L);
        this.mArrowToDownAnim.setFillAfter(true);
        return this.mLoadView;
    }

    @Override
    protected View getLoadView() {
        return this.mLoadView;
    }

    @Override
    protected void onPullToAction() {
        this.mBallLoader.setVisibility(GONE);
        this.mIvArrow.setVisibility(VISIBLE);
        this.mTvRefreshStatus.setText(this.getContext().getString(R.string.l_recycler_pull_to_refresh));
        if (this.mCurState == 4){
            this.mIvArrow.startAnimation(this.mArrowToDownAnim);
        } else if (this.mCurState == 8){
            this.mIvArrow.clearAnimation();
        }
    }

    @Override
    protected void onReleaseToAction() {
        this.mBallLoader.setVisibility(GONE);
        this.mIvArrow.setVisibility(VISIBLE);
        this.mTvRefreshStatus.setText(this.getContext().getString(R.string.l_recycler_release_to_refresh));
        this.mIvArrow.clearAnimation();
        this.mIvArrow.startAnimation(this.mArrowToUpAnim);
    }

    @Override
    protected void onExecuting() {
        this.mBallLoader.setVisibility(VISIBLE);
        this.mIvArrow.clearAnimation();
        this.mIvArrow.setVisibility(GONE);
        this.mTvRefreshStatus.setText(this.getContext().getString(R.string.l_recycler_refreshing));
    }

    @Override
    protected void onDone() {
        this.mIvArrow.clearAnimation();
        this.mIvArrow.setVisibility(GONE);
        this.mBallLoader.setVisibility(GONE);
        this.mTvRefreshStatus.setText(this.getContext().getString(R.string.l_recycler_refreshed));
    }
}
