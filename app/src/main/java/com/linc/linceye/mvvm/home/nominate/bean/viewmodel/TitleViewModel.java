package com.linc.linceye.mvvm.home.nominate.bean.viewmodel;


import com.linc.linceye.R;
import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateItemType;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-12
 */
public class TitleViewModel extends BaseCustomViewModel implements BindingAdapterItem {
    public String title;
    public String actionTitle;

    @Override
    public int getViewType() {
        return R.layout.home_item_title_left_right_view;
    }

    @Override
    public int type() {
        return NominateItemType.TITLE_VIEW;
    }
}
