package com.linc.linceye.mvvm.more.themes.childpager.bean;


import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-23
 */
public class ThemesItemViewModel extends BaseCustomViewModel
{
    public String coverUrl;
    
    public String title;
    
    public String description;

    @Override
    public int type() {
        return 0;
    }
}
