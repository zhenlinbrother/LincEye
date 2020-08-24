package com.linc.base.mvvm.viewmodel;
/**
 * <定义 ViewModel 与 V 的关联> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/10
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IMvvmBaseViewModel<V> {
    /**
     * 关联view
     */
    void attachUi(V view);

    /**
     * 获取view
     */
    V getPageView();

    /**
     * 是否已关联view
     */
    boolean isUiAttach();

    /**
     * 解除关联
     */
    void detachUi();
}
