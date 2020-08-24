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
public class BriefCardViewModel extends BaseCustomViewModel
{
    public String coverUrl;
    
    public String title;
    
    public String description;

    @Override
    public int type() {
        return IDisCoverItemType.THEME_CARD_VIEW;
    }
}
