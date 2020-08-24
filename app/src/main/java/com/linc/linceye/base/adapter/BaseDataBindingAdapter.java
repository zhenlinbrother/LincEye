package com.linc.linceye.base.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/7
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseDataBindingAdapter<M, B extends ViewDataBinding>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected WeakReference<Context> mContext;
    protected final LayoutInflater mInflater;

    private OnItemClickListener<M> mOnItemClickListener;
    private OnItemLongClickListener<M> mOnItemLongClickListener;

    private List<M> items = new ArrayList<>();

    public List<M> getItems() {
        return items;
    }

    public void setItems(List<M> items) {
        this.items = items;
    }

    public BaseDataBindingAdapter(Context context) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
    }

    public BaseDataBindingAdapter(Context context, List<M> data) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.items = data;
    }

    public void setOnItemClickListener(OnItemClickListener<M> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<M> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    protected abstract @LayoutRes int getLayoutResId(int viewType);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(mInflater, getLayoutResId(viewType), parent, false);
        BaseBindingViewHolder holder = new BaseBindingViewHolder(binding.getRoot());
        holder.itemView.setOnClickListener(view -> {
            if (mOnItemClickListener != null){
                int position = holder.getAdapterPosition();
                Log.d("fwerwerwe", "onCreateViewHolder: " + position);
                mOnItemClickListener.onItemClick(items.get(position - 1), position);
            }
        });

        holder.itemView.setOnLongClickListener(view -> {
            if (mOnItemLongClickListener != null){
                int position = holder.getAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(items.get(position - 1), position);
                return true;
            }
            return false;
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, items.get(position), holder);
        if (binding != null){
            binding.executePendingBindings();
        }
    }

    protected abstract void onBindItem(B binding, M item, RecyclerView.ViewHolder holder);

    public interface OnItemClickListener<M>{
        void onItemClick(M item, int position);
    }

    public interface OnItemLongClickListener<M>{
        void onItemLongClick(M item, int position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BaseBindingViewHolder extends RecyclerView.ViewHolder{

        public BaseBindingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
