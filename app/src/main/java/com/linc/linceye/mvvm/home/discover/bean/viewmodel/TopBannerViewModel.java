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
public class TopBannerViewModel extends BaseCustomViewModel
{
    public String bannerUrl;

    @Override
    public int type() {
        return IDisCoverItemType.TOP_BANNER_VIEW;
    }
}
