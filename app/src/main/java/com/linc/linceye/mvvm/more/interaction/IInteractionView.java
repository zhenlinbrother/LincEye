package com.linc.linceye.mvvm.more.interaction;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

public interface IInteractionView extends IBasePagingView {

    /**
     *
     * @param data
     * @param isFirst
     */
    void onDataLoaded(List<ThemesItemViewModel> data, boolean isFirst);
}
