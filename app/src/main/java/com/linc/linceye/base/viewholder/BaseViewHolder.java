package com.linc.linceye.base.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.BR;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

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
