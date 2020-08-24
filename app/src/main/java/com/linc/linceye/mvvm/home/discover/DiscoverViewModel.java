package com.linc.linceye.mvvm.home.discover;

import com.linc.linceye.base.model.BaseModel;
import com.linc.linceye.base.listener.IModelListener;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;

import java.util.List;

public class DiscoverViewModel extends MvvmBaseViewModel<IDiscoverView, DiscoverModel>
        implements IModelListener<List<BaseCustomViewModel>> {

    @Override
    public void onLoadFinish(BaseModel model, List<BaseCustomViewModel> data) {
        if (getPageView() != null){
            if (data != null && data.size() > 0){
                getPageView().onDataLoadFinish(data, false);
            } else {
                getPageView().showEmpty();
            }
        }
    }

    @Override
    public void onLoadFail(BaseModel model, String prompt) {
        if (getPageView() != null){
            getPageView().showFailure(prompt);
        }
    }

    @Override
    public void initModel() {
        model = new DiscoverModel();
        model.register(this);
        model.loadData();
    }

    @Override
    public void detachUi() {
        super.detachUi();
        if (model != null){
            model.unRegister(this);
        }
    }
}
