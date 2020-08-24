package com.linc.linceye.mvvm.player.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.player.bean.viewmodel.ReplyViewModel;

import java.util.List;

public class VideoPlayerAdapter extends CommonSimpleAdapter<BaseCustomViewModel> {
    public VideoPlayerAdapter(Context context, List<BaseCustomViewModel> viewModels) {
        super(context, viewModels);
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
        } else {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.bindData(mData.get(position));
        }
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
