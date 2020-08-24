package com.linc.base.base.listener;
/**
 * <状态回调> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/7
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IStateListener {

    /**
     * 重试回调
     */
    void onRetry();

    /**
     * 加载回调
     */
    void onLoading();

    /**
     * 无数据回调
     */
    void onEmpty();
}
