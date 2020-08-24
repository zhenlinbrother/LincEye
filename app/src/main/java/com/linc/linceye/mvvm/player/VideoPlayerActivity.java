package com.linc.linceye.mvvm.player;

import android.content.res.Configuration;
import android.view.OrientationEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.linc.base.mvvm.activity.SimpleLoadMoreActivity;
import com.linc.linceye.R;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.VideoPlayerActivityBinding;
import com.linc.linceye.mvvm.player.adapter.VideoPlayerAdapter;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;
import com.linceye.video.helper.VideoPlayerHelper;
import com.linceye.video.views.CoverVideoPlayerView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.lang.reflect.Field;
import java.util.List;

public class VideoPlayerActivity
        extends SimpleLoadMoreActivity<VideoPlayerActivityBinding, VideoPlayerViewModel, BaseCustomViewModel>
        implements IVideoPlayerView {

    private VideoHeaderBean headerBean;

    //旋转帮助类
    private OrientationUtils orientationUtils;

    private boolean isPlay = true;

    private boolean isPause;

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(true, headerBean.videoId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.video_player_activity;
    }

    @Override
    protected void initData() {
        super.initData();
        ImmersionBar.with(this).statusBarView(mBinding.topView).init();
        mViewModel.initModel();
        initView(headerBean);
    }

    private void initView(VideoHeaderBean headerBean) {
        mBinding.setBlurred(headerBean.getBlurredUrl());
        mBinding.executePendingBindings();
        initVideoView(headerBean);
    }

    private void initVideoView(VideoHeaderBean headerBean) {
        //设置返回键
        mBinding.cvVideoView.getBackButton().setOnClickListener(v -> finish());
        //全屏辅助
        orientationUtils = new OrientationUtils(this, mBinding.cvVideoView);
        //初始化不打开外部旋转
        orientationUtils.setEnable(false);
        //初始化配置
        VideoPlayerHelper.optionPlayer(mBinding.cvVideoView,
                this,
                headerBean.getPlayerUrl(),
                true,
                headerBean.getVideoTitle());

        mBinding.cvVideoView.setIsTouchWiget(true);

        mBinding.cvVideoView.setVideoAllCallBack(new GSYSampleCallBack(){
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null){
                    orientationUtils.backToProtVideo();
                }
            }
        });

        mBinding.cvVideoView.startPlayLogic();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new VideoPlayerAdapter(this, mData);
    }

    @Override
    protected void initIntent() {
        headerBean = getIntent().getParcelableExtra("video_header_bean");
    }



    @Override
    protected VideoPlayerViewModel getViewModel() {
        return new ViewModelProvider(this).get(VideoPlayerViewModel.class);
    }

    @Override
    public void onDataLoadFinish(List<BaseCustomViewModel> viewModels) {
        if (viewModels == null || viewModels.size() <= 0){
            mBaseAdapter.onEmpty();
            return;
        }

        viewModels.add(0, headerBean);
        mData.addAll(viewModels);
        mBaseAdapter.onSuccess();
        mBaseAdapter.setNoMore();
        mBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreFailure(String message) {
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setNoMore();
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null){
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        mBinding.cvVideoView.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        mBinding.cvVideoView.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.cvVideoView.getGSYVideoManager()
                .setListener(mBinding.cvVideoView.getGSYVideoManager().lastListener());
        mBinding.cvVideoView.getGSYVideoManager().setLastListener(null);
        mBinding.cvVideoView.cancel();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null){
            orientationUtils.releaseListener();
            try {
                //采用反射来解决第三方库中的内存泄漏
                Field mOrientationEventListener = OrientationUtils.class
                        .getDeclaredField("mOrientationEventListener");
                Field contextField = OrientationUtils.class.getField("mActivity");
                contextField.setAccessible(true);
                contextField.set(orientationUtils, null);
                mOrientationEventListener.setAccessible(true);
                OrientationEventListener listener
                        = (OrientationEventListener) mOrientationEventListener.get(orientationUtils);
                Field mSensorEventListener = OrientationEventListener.class
                        .getDeclaredField("mSensorEventListener");
                mSensorEventListener.setAccessible(true);
                mSensorEventListener.set(listener, null);
                Field mSensorManager = OrientationEventListener.class
                        .getDeclaredField("mSensorManager");
                mSensorManager.setAccessible(true);
                mSensorManager.set(listener, null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            orientationUtils = null;
        }
        VideoPlayerHelper.release();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause){
            mBinding.cvVideoView.onConfigurationChanged(this,
                    newConfig,
                    orientationUtils,
                    true,
                    true);
        }
    }

}
