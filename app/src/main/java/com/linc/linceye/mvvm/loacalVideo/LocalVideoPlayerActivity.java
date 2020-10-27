package com.linc.linceye.mvvm.loacalVideo;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.OrientationEventListener;

import androidx.annotation.NonNull;

import com.gyf.immersionbar.ImmersionBar;
import com.linc.base.mvvm.activity.BaseMvvmActivity;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.linceye.R;
import com.linc.linceye.databinding.ActivityLocalVideoPlayerBinding;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;
import com.linceye.video.helper.VideoPlayerHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.lang.reflect.Field;

public class LocalVideoPlayerActivity extends BaseMvvmActivity<IMvvmBaseViewModel, ActivityLocalVideoPlayerBinding> {

    private VideoHeaderBean headerBean;
    private OrientationUtils orientationUtils;

    private boolean isPlay = true;
    private boolean isPause;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_video_player;
    }

    @Override
    protected IMvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void onPause() {
        mBinding.videoPlayer.onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        mBinding.videoPlayer.onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this)
                .statusBarView(mBinding.topView)
                .init();
        orientationUtils = new OrientationUtils(this, mBinding.videoPlayer);
        orientationUtils.setEnable(false);

        mBinding.videoPlayer.setUp(headerBean.playerUrl, true, headerBean.videoTitle);

        mBinding.videoPlayer.setIsTouchWiget(true);
        mBinding.videoPlayer.setAutoFullWithSize(true);
        mBinding.videoPlayer.setReleaseWhenLossAudio(true);
        
        mBinding.videoPlayer.setVideoAllCallBack(new GSYSampleCallBack(){
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                orientationUtils.setEnable(true);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }
        });

        mBinding.videoPlayer.startPlayLogic();

        mBinding.videoPlayer.getFullscreenButton().setOnClickListener(v -> {
            mBinding.videoPlayer.startWindowFullscreen(this, false, false);
        });
    }

    @Override
    protected void initIntent() {
        headerBean = getIntent().getParcelableExtra("HeaderBean");
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
    public void showContent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showFailure(String message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.videoPlayer.getGSYVideoManager()
                .setListener(mBinding.videoPlayer.getGSYVideoManager().lastListener());
        mBinding.videoPlayer.getGSYVideoManager().setLastListener(null);
        //mBinding.videoPlayer.cancel();
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
        if (isPlay && !isPause){
            mBinding.videoPlayer.onConfigurationChanged(this,
                    newConfig,
                    orientationUtils,
                    true,
                    true);
        }
    }
}
