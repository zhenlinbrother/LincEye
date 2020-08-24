package com.linc.lrecyclerview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.lrecyclerview.config.LRecyclerConfig;
import com.linc.lrecyclerview.config.LRecyclerViewManager;
import com.linc.lrecyclerview.view.OrdinaryLoadMoreView;
import com.linc.lrecyclerview.view.OrdinaryRefreshLoadView;
import com.linc.lrecyclerview.view.base.IBaseLoadMoreView;
import com.linc.lrecyclerview.view.base.IBaseRefreshLoadView;


/**
 * <装载了上拉和下拉刷新的适配器> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LRefreshAndLoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mIsOpenRefresh = true;
    private boolean mIsOpenLoadMore = true;
    private IBaseRefreshLoadView mRefreshLoadView;
    private IBaseLoadMoreView mLoadMoreView;
    private RecyclerView.Adapter mRealAdapter;
    private LRefreshAndLoadMoreAdapter.OnRefreshListener mOnRefreshListener;
    private LRefreshAndLoadMoreAdapter.OnLoadMoreListener mOnLoadMoreListener;

    public LRefreshAndLoadMoreAdapter(Context context, RecyclerView.Adapter adapter) {
        this.mRealAdapter = adapter;
        if (this.mRefreshLoadView == null){
            if (LRecyclerViewManager.getInstance().getRefreshLoadView() == null){
                this.mRefreshLoadView = new OrdinaryRefreshLoadView(context);
            } else {
                this.mRefreshLoadView = LRecyclerViewManager.getInstance().getRefreshLoadView();
            }
        }

        if (this.mLoadMoreView == null){
            if (LRecyclerViewManager.getInstance().getLoadMoreView() == null){
                this.mLoadMoreView = new OrdinaryLoadMoreView(context);
            } else {
                this.mLoadMoreView = LRecyclerViewManager.getInstance().getLoadMoreView();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return LRefreshAndLoadMoreAdapter.this.getItemViewType(position) != LRecyclerConfig.HEAD
                            && LRefreshAndLoadMoreAdapter.this.getItemViewType(position) != LRecyclerConfig.FOOT
                            ? 1 : gridLayoutManager.getSpanCount();
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LRecyclerConfig.HEAD){
            if (this.mOnRefreshListener != null){
                this.mRefreshLoadView.setOnRefreshListener(this.mOnRefreshListener);
            }

            return new LRefreshAndLoadMoreAdapter.LRefreshViewHolder(this.mRefreshLoadView);
        } else if (viewType == LRecyclerConfig.FOOT){
            if (this.mOnLoadMoreListener != null){
                this.mLoadMoreView.setOnLoadMoreListener(this.mOnLoadMoreListener);
            }

            return new LRefreshAndLoadMoreAdapter.LLoadMoreViewHolder(this.mLoadMoreView);
        } else {
            return this.mRealAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof LRefreshAndLoadMoreAdapter.LRefreshViewHolder) && !(holder instanceof LRefreshAndLoadMoreAdapter.LLoadMoreViewHolder)) {
            this.mRealAdapter.onBindViewHolder(holder, this._getRealPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        int count = this.mRealAdapter.getItemCount();
        if (this.mIsOpenRefresh) {
            ++count;
        }

        if (this.mIsOpenLoadMore) {
            ++count;
        }

        return count;
    }

    private int _getRealPosition(int position){
        return this.mIsOpenRefresh ? position - 1 : position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.mIsOpenRefresh){
            return LRecyclerConfig.HEAD;
        } else {
            return position == this.getItemCount() - 1 && this.mIsOpenLoadMore
                    ? LRecyclerConfig.FOOT
                    : this.mRealAdapter.getItemViewType(this._getRealPosition(position));
        }

    }

    public void setRefreshComplete() {
        if (this.getRefreshLoadView() != null) {
            this.getRefreshLoadView().refreshComplete();
            this.notifyDataSetChanged();
        }

    }

    public void setLoadComplete() {
        if (this.mIsOpenLoadMore) {
            this.mLoadMoreView.loadComplete();
        }

    }

    public void setLoadError() {
        if (this.mIsOpenLoadMore) {
            this.mLoadMoreView.loadError();
        }

    }

    public void resetLoadMore() {
        if (this.mIsOpenLoadMore) {
            this.mLoadMoreView.reset();
        }

    }

    public void setNoMore() {
        if (this.mIsOpenLoadMore) {
            this.mLoadMoreView.noMore();
        }

    }

    public void setIsOpenRefresh(boolean mIsOpenRefresh) {
        this.mIsOpenRefresh = mIsOpenRefresh;
    }

    public void setIsOpenLoadMore(boolean mIsOpenLoadMore) {
        this.mIsOpenLoadMore = mIsOpenLoadMore;
    }

    public boolean isOpenRefresh() {
        return mIsOpenRefresh;
    }


    public boolean isOpenLoadMore() {
        return mIsOpenLoadMore;
    }


    public IBaseRefreshLoadView getRefreshLoadView() {
        return mRefreshLoadView;
    }

    public void setRefreshLoadView(IBaseRefreshLoadView mRefreshLoadView) {
        this.mRefreshLoadView = mRefreshLoadView;
    }

    public IBaseLoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public void setLoadMoreView(IBaseLoadMoreView mLoadMoreView) {
        this.mLoadMoreView = mLoadMoreView;
    }

    public RecyclerView.Adapter getRealAdapter() {
        return mRealAdapter;
    }

    public void setRealAdapter(RecyclerView.Adapter mRealAdapter) {
        this.mRealAdapter = mRealAdapter;
    }

    public OnRefreshListener getOnRefreshListener() {
        return mOnRefreshListener;
    }

    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoading();
    }

    public interface OnRefreshListener {
        void onRefreshing();
    }

    public static class LLoadMoreViewHolder extends RecyclerView.ViewHolder{

        public LLoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class LRefreshViewHolder extends RecyclerView.ViewHolder{

        public LRefreshViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
