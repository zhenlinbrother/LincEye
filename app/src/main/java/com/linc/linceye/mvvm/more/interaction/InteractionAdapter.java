package com.linc.linceye.mvvm.more.interaction;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.databinding.MoreItemThemesViewBinding;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

public class InteractionAdapter extends SimpleDataBindingAdapter<ThemesItemViewModel, MoreItemThemesViewBinding> {
    protected InteractionAdapter(Context context, int layout, List<ThemesItemViewModel> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(MoreItemThemesViewBinding binding, ThemesItemViewModel item, RecyclerView.ViewHolder holder) {
        binding.setViewModel(item);
        binding.executePendingBindings();
    }
}
