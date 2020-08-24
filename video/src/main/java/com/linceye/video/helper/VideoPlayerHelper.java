package com.linceye.video.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.linceye.video.views.CoverVideoPlayerView;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;

/**
 * <播放帮助类> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class VideoPlayerHelper {

    private static CoverVideoPlayerView mVideoView;
    private static GSYMediaPlayerListener mMediaPlayerListener;

    /**
     * 播放前初始化配置
     */
    public static void optionPlayer(final CoverVideoPlayerView gsyVideoPlayer,
                                    final Context context,
                                    String url,
                                    boolean cache,
                                    String title){
        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(context, false, false);
            }
        });
        //是否根据视频尺寸，自动选择竖屏全屏或者
        gsyVideoPlayer.setAutoFullWithSize(true);
        //音频焦点冲突时是否释放
        gsyVideoPlayer.setReleaseWhenLossAudio(true);
        //全屏动画
        gsyVideoPlayer.setShowFullAnimation(false);
        //小屏时不触碰滑动
        gsyVideoPlayer.setIsTouchWiget(false);

        gsyVideoPlayer.setVideoUrl(url);

        gsyVideoPlayer.setVideoCache(cache);

        gsyVideoPlayer.setVideoTitle(title);
    }

    public static void savePlayState(CoverVideoPlayerView videoPlayerView){
        mVideoView = videoPlayerView.saveState();
        mMediaPlayerListener = videoPlayerView;
    }

    public static void clonePlayState(CoverVideoPlayerView videoPlayerView) {
        videoPlayerView.cloneState(mVideoView);
    }

    public static void release() {
        if (mMediaPlayerListener != null) {
            mMediaPlayerListener.onAutoCompletion();
        }
        mVideoView = null;
        mMediaPlayerListener = null;
    }
}
