package com.linc.linceye.mvvm.more.themes.childpager;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IThemeContentView extends IBasePagingView {

    /**
     * 数据加载完成
     * @param viewModels data
     * @param isFirstPage   是否第一页数据
     */
    void onDataLoaded(List<ThemesItemViewModel> viewModels, boolean isFirstPage);
}
