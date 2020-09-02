package com.linc.linceye.mvvm.community.attention;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import java.util.List;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IAttentionView extends IBasePagingView {
    /**
     * 数据加载成功
     * @param viewModels 数据
     * @param isFirst 是否第一次
     */
    void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst);
}
