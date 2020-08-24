package com.linc.linceye.mvvm.home.daily;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateAdapter;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateItemType;

import java.util.List;

public class DailyAdapter extends CommonSimpleAdapter<BaseCustomViewModel> {

    public DailyAdapter(Context context, List<BaseCustomViewModel> viewModels) {
        super(context, viewModels);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        RecyclerView.ViewHolder holder = null;

        switch (viewType){
            case NominateItemType.FOLLOW_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_follow_card_view, parent, false);
                holder = new NominateAdapter.FollowCardViewHolder(binding);
                break;
            case NominateItemType.SINGLE_TITLE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_single_title_view, parent, false);
                holder = new NominateAdapter.SingleTitleViewHolder(binding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NominateAdapter.FollowCardViewHolder){
            NominateAdapter.FollowCardViewHolder followCardViewHolder
                    = (NominateAdapter.FollowCardViewHolder) holder;
            followCardViewHolder.bindData(mData.get(position));
        } else {
            NominateAdapter.SingleTitleViewHolder singleTitleViewHolder
                    = (NominateAdapter.SingleTitleViewHolder) holder;
            singleTitleViewHolder.bindData(mData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type();
    }
}
