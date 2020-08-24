package com.linc.linceye.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;


import com.linc.linceye.BR;
import com.linc.linceye.base.listener.BindingAdapterItem;


import java.util.ArrayList;
import java.util.List;

/**
 * <基于多种类型的 RecyclerView 的适配器> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class MultiBindingAdapter extends RecyclerView.Adapter<MultiBindingAdapter.BindingHolder> {

    protected List<BindingAdapterItem> items = new ArrayList<>();

    public List<BindingAdapterItem> getItems() {
        return items;
    }

    public void setItems(List<BindingAdapterItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder holder, int position) {
        holder.bindData(items.get(position));
        setOnClickEvent(holder, items.get(position));
    }

    public abstract void setOnClickEvent(BindingHolder holder, BindingAdapterItem item);

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public static class BindingHolder extends RecyclerView.ViewHolder{

        ViewDataBinding binding;
        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(BindingAdapterItem item){
            binding.setVariable(BR.viewModel, item);
            binding.executePendingBindings();
        }
    }

}
