package com.linc.base.base.listener;


import com.linc.base.base.adapter.BitFrameAdapter;

/**
 * <状态页视图回调> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/7
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface StateViewHolderListener {

    void handleEmptyViewHolder(BitFrameAdapter.EmptyViewHolder holder);

    void handleRetryViewHolder(BitFrameAdapter.RetryViewHolder holder);

    void handleLoadingViewHolder(BitFrameAdapter.LoadingViewHolder holder);
}
