package com.linc.linceye.mvvm.more.push;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.more.push.bean.MessageViewModel;

import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IPushView extends IBasePagingView {

    /**
     * 数据加载成功
     * @param data 数据
     * @param isFirstPage 是否第一页
     */
    void onDataLoaded(List<MessageViewModel> data, boolean isFirstPage);
}
