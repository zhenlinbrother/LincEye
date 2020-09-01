package com.linc.linceye.mvvm.community.recommend;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;


import java.util.List;

public interface IRecommendView extends IBasePagingView {
    /**
     * <数据加载成功> <功能详细描述>
     *
     * @author linc
     * @version 2020/8/24
     * @see [相关类/方法]
     * @since [产品/模块版本]
     */
    void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst);
}
