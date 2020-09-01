package com.linc.linceye.mvvm.community.recommend.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.linc.base.base.utils.UIUtils;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.CommunityItemCommunityViewBinding;
import com.linc.linceye.databinding.CommunityItemSquareCardViewBinding;
import com.linc.linceye.mvvm.community.recommend.bean.HorizontalScrollCard;
import com.linc.linceye.mvvm.community.recommend.bean.viewmodel.CloumnsCardViewModel;
import com.linc.linceye.mvvm.community.recommend.bean.viewmodel.IRecommendItemType;
import com.linc.linceye.utils.DensityUtils;
import com.linc.linceye.utils.RecyclerItemDecoration;

import java.util.List;

public class RecommendAdapter extends CommonSimpleAdapter<BaseCustomViewModel> {
    public RecommendAdapter(Context context, List<BaseCustomViewModel> viewModels) {
        super(context, viewModels);
    }

    public void setData(List<BaseCustomViewModel> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case IRecommendItemType.SQUARE_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.community_item_square_card_view, parent, false);
                holder = new SquareCardViewHolder(binding);
                break;
            case IRecommendItemType.COMMUNITY_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.community_item_community_view, parent, false);
                holder = new CommunityCardViewHolder(binding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SquareCardViewHolder){
            SquareCardViewHolder squareCardViewHolder = (SquareCardViewHolder) holder;
            squareCardViewHolder.bindData(mData.get(position));

            CommunityItemSquareCardViewBinding binding = (CommunityItemSquareCardViewBinding) squareCardViewHolder.getBinding();
            StaggeredGridLayoutManager.LayoutParams layoutParams
                    = new StaggeredGridLayoutManager.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtils.dip2px(mContext.get(), 80));
            layoutParams.setFullSpan(true);
            squareCardViewHolder.itemView.setLayoutParams(layoutParams);
            GridLayoutManager layoutManager = new GridLayoutManager(mContext.get(), 1);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.rvSquareView.setLayoutManager(layoutManager);
            binding.rvSquareView.addItemDecoration(new RecyclerItemDecoration(0, 0, DensityUtils.dip2px(mContext.get(), 5), 0));

            HorizontalScrollCard scrollCard = (HorizontalScrollCard) mData.get(position);
            SquareCardAdapter adapter = new SquareCardAdapter(mContext.get(), R.layout.community_item_square_item_card_view, scrollCard.getData().getItemList());
            binding.rvSquareView.setAdapter(adapter);
        } else {
            CommunityCardViewHolder communityCardViewHolder = (CommunityCardViewHolder) holder;
            communityCardViewHolder.bindData(mData.get(position));

            CommunityItemCommunityViewBinding binding = (CommunityItemCommunityViewBinding) communityCardViewHolder.getBinding();
            ViewGroup.LayoutParams layoutParams = binding.ivCoverBg.getLayoutParams();
            CloumnsCardViewModel viewModel = (CloumnsCardViewModel) mData.get(position);
            int itemWidth = UIUtils.getScreenWidth(mContext.get()) / 2;
            float scale = (itemWidth + 0f) / viewModel.imgHeight;
            layoutParams.height = (int) (viewModel.imgHeight * scale);
            binding.ivCoverBg.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type();
    }

    public static class SquareCardViewHolder extends BaseViewHolder{

        public SquareCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class CommunityCardViewHolder extends BaseViewHolder{

        public CommunityCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }
}
