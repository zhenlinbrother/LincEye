package com.linc.base.mvvm.viewholder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.BR;
import com.linc.base.mvvm.viewmodel.BaseCustomViewModel;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    ViewDataBinding binding;
    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(BaseCustomViewModel item){
        binding.setVariable(BR.viewModel, item);
        binding.executePendingBindings();
    }

    public ViewDataBinding getBinding() {
        return binding;
    }
}
