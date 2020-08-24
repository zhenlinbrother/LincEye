package com.linc.linceye.mvvm.home.discover.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.databinding.HomeItemCategoryItemSubjectCardViewBinding;
import com.linc.linceye.mvvm.home.discover.bean.SquareCard;

import java.util.List;

public class SubjectItemAdapter extends SimpleDataBindingAdapter<SquareCard, HomeItemCategoryItemSubjectCardViewBinding> {
    protected SubjectItemAdapter(Context context, int layout, List<SquareCard> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(HomeItemCategoryItemSubjectCardViewBinding binding, SquareCard item, RecyclerView.ViewHolder holder) {
        binding.setViewModel(item);
    }
}
