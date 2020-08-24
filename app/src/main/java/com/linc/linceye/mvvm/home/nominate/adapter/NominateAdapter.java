package com.linc.linceye.mvvm.home.nominate.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


import com.linc.linceye.R;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.HomeItemFollowCardViewBinding;
import com.linc.linceye.databinding.HomeItemVideoCardViewBinding;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.FollowCardViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.VideoCardViewModel;
import com.linc.linceye.mvvm.player.VideoPlayerActivity;
import com.linc.linceye.mvvm.player.bean.VideoHeaderBean;


import java.lang.ref.WeakReference;
import java.util.List;

public class NominateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseCustomViewModel> mData;
    private WeakReference<Context> mContext;
    private LayoutInflater mInflater;

    public void setData(List<BaseCustomViewModel> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public NominateAdapter(Context context, List<BaseCustomViewModel> data) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        RecyclerView.ViewHolder holder;
        switch (viewType){
            case NominateItemType.BANNER_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_banner_view, parent, false);
                holder = new BannerViewHolder(binding);
                break;
            case NominateItemType.TITLE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_title_left_right_view, parent, false);
                holder = new TitleViewHolder(binding);
                break;
            case NominateItemType.FOLLOW_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_follow_card_view, parent, false);
                holder = new FollowCardViewHolder(binding);
                break;
            case NominateItemType.SINGLE_TITLE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_single_title_view, parent, false);
                holder = new SingleTitleViewHolder(binding);
                break;
            case NominateItemType.VIDEO_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_video_card_view, parent, false);
                holder = new VideoCardViewHolder(binding);
                break;
            default:
                holder = null;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder){
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.bindData(mData.get(position));
        } else if (holder instanceof TitleViewHolder){
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.bindData(mData.get(position));
        } else if (holder instanceof FollowCardViewHolder){
            FollowCardViewHolder followCardViewHolder = (FollowCardViewHolder) holder;
            followCardViewHolder.bindData(mData.get(position));

            FollowCardViewModel cardViewModel = (FollowCardViewModel) mData.get(position);
            HomeItemFollowCardViewBinding binding = (HomeItemFollowCardViewBinding) followCardViewHolder.getBinding();
            binding.ivVideoCover.setOnClickListener(v -> {
                VideoHeaderBean headerBean = new VideoHeaderBean(
                        cardViewModel.title, cardViewModel.description,
                        cardViewModel.video_description,
                        cardViewModel.collectionCount, cardViewModel.shareCount,
                        cardViewModel.authorUrl, cardViewModel.nickName,
                        cardViewModel.userDescription, cardViewModel.playerUrl,
                        cardViewModel.blurredUrl, cardViewModel.videoId
                );

                Bundle bundle = new Bundle();
                bundle.putParcelable("video_header_bean", headerBean);
                Intent intent = new Intent(mContext.get(), VideoPlayerActivity.class);
                intent.putExtras(bundle);
                mContext.get().startActivity(intent);
            });

        } else if (holder instanceof SingleTitleViewHolder){
            SingleTitleViewHolder singleTitleViewHolder = (SingleTitleViewHolder) holder;
            singleTitleViewHolder.bindData(mData.get(position));
        } else if (holder instanceof VideoCardViewHolder){
            VideoCardViewHolder videoCardViewHolder = (VideoCardViewHolder) holder;
            videoCardViewHolder.bindData(mData.get(position));

            VideoCardViewModel cardViewModel = (VideoCardViewModel) mData.get(position);
            HomeItemVideoCardViewBinding binding = (HomeItemVideoCardViewBinding) videoCardViewHolder.getBinding();
            binding.ivVideoCover.setOnClickListener(v -> {
                VideoHeaderBean headerBean = new VideoHeaderBean(
                        cardViewModel.title, cardViewModel.description,
                        cardViewModel.video_description,
                        cardViewModel.collectionCount, cardViewModel.shareCount,
                        cardViewModel.authorUrl, cardViewModel.nickName,
                        cardViewModel.userDescription, cardViewModel.playerUrl,
                        cardViewModel.blurredUrl, cardViewModel.videoId
                );

                Bundle bundle = new Bundle();
                bundle.putParcelable("video_header_bean", headerBean);
                Intent intent = new Intent(mContext.get(), VideoPlayerActivity.class);
                intent.putExtras(bundle);
                mContext.get().startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 :mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type();
    }

    public static class BannerViewHolder extends BaseViewHolder{
        public BannerViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class TitleViewHolder extends BaseViewHolder{

        public TitleViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class FollowCardViewHolder extends BaseViewHolder{

        public FollowCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class SingleTitleViewHolder extends BaseViewHolder{

        public SingleTitleViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class VideoCardViewHolder extends BaseViewHolder{

        public VideoCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }
}
