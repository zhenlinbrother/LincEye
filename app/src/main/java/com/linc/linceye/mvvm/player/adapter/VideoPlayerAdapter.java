package com.linc.linceye.mvvm.player.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.download.bean.CurStatus;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.jerry.JerryDownload;
import com.linc.download.listener.DownloadListener;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.PlayerItemVideoHeaderViewBinding;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;
import com.linc.linceye.mvvm.player.bean.viewmodel.ReplyViewModel;

import java.util.List;

public class VideoPlayerAdapter extends CommonSimpleAdapter<BaseCustomViewModel> {

    private final VideoHeaderBean mHeaderBean;

    private DownloadInfo mDownloadInfo;
    public VideoPlayerAdapter(Context context,
                              List<BaseCustomViewModel> viewModels,
                              VideoHeaderBean headerBean) {
        super(context, viewModels);
        this.mHeaderBean = headerBean;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case IVideoItemType.TITLE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.player_item_title_white_view, parent, false);
                holder = new TitleViewHolder(binding);
                break;
            case IVideoItemType.NOMINATE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.player_item_video_card_white_view, parent, false);
                holder = new NominateViewHolder(binding);
                break;
            case IVideoItemType.REPLY_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.player_item_reply_view, parent, false);
                holder = new ReplyViewHolder(binding);
                break;
            case IVideoItemType.VIDEO_DESCRIPTION:
                binding = DataBindingUtil.inflate(mInflater, R.layout.player_item_video_header_view, parent, false);
                holder = new VideoDescriptionViewHolder(binding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder){
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.bindData(mData.get(position));
        } else if (holder instanceof NominateViewHolder){
            NominateViewHolder nominateViewHolder = (NominateViewHolder) holder;
            nominateViewHolder.bindData(mData.get(position));
        } else if (holder instanceof VideoDescriptionViewHolder){
            VideoDescriptionViewHolder videoDescriptionViewHolder = (VideoDescriptionViewHolder) holder;
            videoDescriptionViewHolder.bindData(mData.get(position));

            PlayerItemVideoHeaderViewBinding mBinding
                    = (PlayerItemVideoHeaderViewBinding) videoDescriptionViewHolder.getBinding();
            mBinding.tvRealCount.setOnClickListener(v -> {
                //执行下载功能
                mDownloadInfo = JerryDownload.getInstance().download(
                        mHeaderBean.getPlayerUrl(),
                        mHeaderBean.getVideoTitle(),
                        mHeaderBean.coverUrl);

                setDownloadListener(mBinding);
            });

            if (mDownloadInfo != null) {

                if (mDownloadInfo.isCurStatusContains(CurStatus.ERROR)
                        || mDownloadInfo.isCurStatusContains(CurStatus.TIP)
                        || mDownloadInfo.isCurStatusContains(CurStatus.PAUSE)){
                    mBinding.tvRealCount.setText("缓存");
                    return;
                }

                if (mDownloadInfo.isCurStatusContains(CurStatus.FINISH)) {
                    mBinding.tvRealCount.setText("已缓存");
                    return;
                }

                if (mDownloadInfo.isCurStatusContains(CurStatus.DOWNLOADING)){
                    mBinding.tvRealCount.setText(mDownloadInfo.getPercent() + "%");
                    return;
                }
            }

        } else {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.bindData(mData.get(position));
        }
    }

    private void setDownloadListener(PlayerItemVideoHeaderViewBinding binding) {
        mDownloadInfo.setListener(new DownloadListener() {
            @Override
            public void onPause() {
                binding.tvRealCount.setText("继续");
            }

            @Override
            public void onWaitting() {

            }

            @Override
            public void onInit() {

            }

            @Override
            public void onDownloading() {
                binding.tvRealCount.setText(mDownloadInfo.getPercent() + "%");
            }

            @Override
            public void onTip() {
                binding.tvRealCount.setText("缓存");
            }

            @Override
            public void onError() {
                binding.tvRealCount.setText("缓存");
            }

            @Override
            public void onFinish() {
                binding.tvRealCount.setText("已缓存");
            }

            @Override
            public void onProgress() {
                binding.tvRealCount.setText(mDownloadInfo.getPercent() + "%");
            }
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof VideoDescriptionViewHolder) {
            VideoDescriptionViewHolder videoDescriptionViewHolder = (VideoDescriptionViewHolder) holder;

            PlayerItemVideoHeaderViewBinding binding = (PlayerItemVideoHeaderViewBinding) videoDescriptionViewHolder.getBinding();
            if (mDownloadInfo != null) {
                if (mDownloadInfo.isCurStatusContains(CurStatus.ERROR)
                        || mDownloadInfo.isCurStatusContains(CurStatus.TIP)
                        || mDownloadInfo.isCurStatusContains(CurStatus.PAUSE)){
                    binding.tvRealCount.setText("缓存");
                    return;
                }

                if (mDownloadInfo.isCurStatusContains(CurStatus.FINISH)) {
                    binding.tvRealCount.setText("已缓存");
                    return;
                }

                if (mDownloadInfo.isCurStatusContains(CurStatus.DOWNLOADING)){
                    binding.tvRealCount.setText(mDownloadInfo.getPercent() + "%");
                    return;
                }
            }

        }
        super.onViewAttachedToWindow(holder);
    }

    public static class VideoDescriptionViewHolder extends BaseViewHolder{

        public VideoDescriptionViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class TitleViewHolder extends BaseViewHolder{

        public TitleViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class NominateViewHolder extends BaseViewHolder{

        public NominateViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class ReplyViewHolder extends BaseViewHolder{

        public ReplyViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type();
    }
}
