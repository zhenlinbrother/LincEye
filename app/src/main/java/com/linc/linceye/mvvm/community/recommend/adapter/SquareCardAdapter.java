package com.linc.linceye.mvvm.community.recommend.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.databinding.CommunityItemSquareItemCardViewBinding;
import com.linc.linceye.mvvm.community.recommend.bean.HorizontalScrollCard;
import com.linc.linceye.mvvm.community.recommend.bean.SquareContentCard;

import java.util.List;

public class SquareCardAdapter extends SimpleDataBindingAdapter<SquareContentCard,
        CommunityItemSquareItemCardViewBinding> {

    protected SquareCardAdapter(Context context, int layout, List<SquareContentCard> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(CommunityItemSquareItemCardViewBinding binding, SquareContentCard item, RecyclerView.ViewHolder holder) {
        binding.setViewModel(item);
        binding.executePendingBindings();
    }
}
