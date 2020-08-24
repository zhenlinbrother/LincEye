package com.linc.linceye.mvvm.home.nominate.bean.viewmodel;

import com.linc.linceye.R;
import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateItemType;

import java.util.ArrayList;


/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-12
 */
public class BannerCardViewModel extends BaseCustomViewModel implements BindingAdapterItem {
    private ArrayList<String> banners;

    @Override
    public int getViewType() {
        return R.layout.home_item_banner_view;
    }

    public ArrayList<String> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<String> banners) {
        this.banners = banners;

    }

    @Override
    public int type() {
        return NominateItemType.BANNER_VIEW;
    }
}
