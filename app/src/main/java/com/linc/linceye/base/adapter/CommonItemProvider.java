package com.linc.linceye.base.adapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linc.linceye.BR;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CommonItemProvider<V extends ViewDataBinding, VM extends BaseCustomViewModel> extends BaseItemProvider<BaseCustomViewModel> {

    protected V binding;
    protected VM mModel;
    @Override
    public void onViewHolderCreated(@NotNull BaseViewHolder viewHolder, int viewType) {
        DataBindingUtil.bind(viewHolder.itemView);
    }

    @Override
    public void convert(@NotNull BaseViewHolder baseViewHolder, @Nullable BaseCustomViewModel baseCustomViewModel) {
        if (baseCustomViewModel == null){
            return;
        }

        binding = baseViewHolder.getBinding();

        if (binding != null){
            mModel = (VM) baseCustomViewModel;
            initEvent();
            binding.setVariable(BR.viewModel, mModel);
            binding.executePendingBindings();
        }
    }

    public abstract void initEvent();
}
