package com.linc.linceye.mvvm.home.daily;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import java.util.List;

public interface IDailyView extends IBasePagingView {

    /**
     * 数据加载成功
     * @param viewModels
     * @param
     */
    void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst);
}
