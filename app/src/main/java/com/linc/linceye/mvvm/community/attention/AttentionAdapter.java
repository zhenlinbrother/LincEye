package com.linc.linceye.mvvm.community.attention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.base.utils.DateTimeUtils;
import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.CommunityItemAttentionCardViewBinding;
import com.linc.linceye.mvvm.community.attention.bean.AttentionCardBean;
import com.linc.linceye.mvvm.community.attention.bean.AttentionCardViewModel;
import com.linc.linceye.mvvm.player.VideoPlayerActivity;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import java.util.List;

/**
 * <社区-关注-适配器> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AttentionAdapter extends SimpleDataBindingAdapter<BaseCustomViewModel, CommunityItemAttentionCardViewBinding> {
    protected AttentionAdapter(Context context, int layout, List<BaseCustomViewModel> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(CommunityItemAttentionCardViewBinding binding,
                              BaseCustomViewModel viewModel,
                              RecyclerView.ViewHolder holder) {

        AttentionCardViewModel item = (AttentionCardViewModel) viewModel;
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        binding.videoItemPlayer.loadCoverImage(item.coverUrl, 0);
        binding.videoItemPlayer.rvContent.setOnClickListener(v -> {
            VideoHeaderBean headerBean = new VideoHeaderBean(
                    item.title,
                    item.category,
                    item.description,
                    item.collectionCount,
                    item.shareCount,
                    item.avatarUrl,
                    item.issuerName,
                    item.authorDescription,
                    item.playUrl,
                    item.blurredUrl,
                    item.videoId,
                    item.coverUrl
            );
            Bundle bundle = new Bundle();
            bundle.putParcelable("video_header_bean", headerBean);
            Intent intent = new Intent(mContext.get(), VideoPlayerActivity.class);
            intent.putExtras(bundle);
            mContext.get().startActivity(intent);
        });

        gsyVideoOptionBuilder.setIsTouchWiget(false)
                .setUrl(item.playUrl)
                .setVideoTitle(item.title)
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag("2")
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(holder.getAdapterPosition())
                .setVideoAllCallBack(new GSYSampleCallBack(){
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!binding.videoItemPlayer.isIfCurrentIsFullscreen()){
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(false);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        binding.videoItemPlayer.getCurrentPlayer()
                                .getTitleTextView()
                                .setText((String) objects[0]);
                    }
                })
                .build(binding.videoItemPlayer);

        //增加title
        binding.videoItemPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        binding.videoItemPlayer.getBackButton().setVisibility(View.GONE);

        binding.tvReleaseTime.setText(DateTimeUtils.getDate(String.valueOf(item.releaseTime), "HH:mm"));

        binding.setViewModel(item);
        binding.executePendingBindings();
    }
}
