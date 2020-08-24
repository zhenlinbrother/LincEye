package com.linc.base.mvvm.view;
/**
 * <界面UI显示切换> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/10
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IBaseView {
    /**
     * 显示内容
     */
    void showContent();

    /**
     * 显示加载提示
     */
    void showLoading();

    /**
     * 显示空页面
     */
    void showEmpty();

    /**
     * 刷新失败
     */
    void showFailure(String message);



}
