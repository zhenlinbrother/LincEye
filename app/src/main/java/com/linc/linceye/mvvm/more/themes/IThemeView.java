package com.linc.linceye.mvvm.more.themes;

import com.linc.base.mvvm.view.IBaseView;
import com.linc.linceye.mvvm.more.themes.bean.Tabs;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IThemeView extends IBaseView {

    /**
     * 数据加载成功
     * @param tabs
     */
    void onDataLoaded(List<Tabs> tabs);
}
