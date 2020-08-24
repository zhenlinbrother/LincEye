package com.linc.linceye.mvvm.home.discover;

import com.linc.base.mvvm.view.IBaseView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import java.util.List;

public interface IDiscoverView extends IBaseView {

    void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isEmpty);
}
