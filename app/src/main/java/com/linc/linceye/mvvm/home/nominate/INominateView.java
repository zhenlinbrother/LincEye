package com.linc.linceye.mvvm.home.nominate;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import java.util.ArrayList;
import java.util.List;

public interface INominateView extends IBasePagingView {

    void onDataLoadFinish(ArrayList<BaseCustomViewModel> viewModels, boolean isFirstPage);
}
