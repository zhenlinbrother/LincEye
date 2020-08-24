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
public class SingleTitleViewModel extends BaseCustomViewModel implements BindingAdapterItem {
    public String title;

    @Override
    public int getViewType() {
        return R.layout.home_item_single_title_view;
    }

    @Override
    public int type() {
        return NominateItemType.SINGLE_TITLE_VIEW;
    }
}
