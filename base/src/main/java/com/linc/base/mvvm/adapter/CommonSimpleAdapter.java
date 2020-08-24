package com.linc.base.mvvm.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class CommonSimpleAdapter<DATA> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected final List<DATA> mData;

    protected final LayoutInflater mInflater;

    protected final WeakReference<Context> mContext;

    public CommonSimpleAdapter(Context context, List<DATA> dataList) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = new WeakReference<>(context);
        this.mData = dataList;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
