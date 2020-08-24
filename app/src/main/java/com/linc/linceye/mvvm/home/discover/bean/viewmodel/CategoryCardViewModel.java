package com.linc.linceye.mvvm.home.discover.bean.viewmodel;


import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-15
 */
public class CategoryCardViewModel extends BaseCustomViewModel
{
    public String title;
    
    public String actionTitle;

    @Override
    public int type() {
        return 0;
    }
}
