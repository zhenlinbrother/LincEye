package com.linc.linceye.mvvm.home.daily;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;

import java.util.List;

public class DailyViewModel extends MvvmBaseViewModel<IDailyView, DailyModel>
        implements IPagingModelListener<List<BaseCustomViewModel>> {

    public DailyViewModel() {
        initModel();
    }

    @Override
    public void onLoadFinish(BasePagingModel model, List<BaseCustomViewModel> data, boolean isEmpty, boolean isFirstPage) {
        if (getPageView() != null){

            if (isEmpty){

                if (isFirstPage){
                    getPageView().showEmpty();
                } else {
                    getPageView().onLoadMoreEmpty();
                }
            } else {
                getPageView().onDataLoadFinish(data, isFirstPage);
            }
        }
    }

    @Override
    public void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage) {
        if (getPageView() != null){

            if (isFirstPage){
                getPageView().showFailure(prompt);
            } else {
                getPageView().onLoadMoreFailure(prompt);
            }
        }
    }

    @Override
    public void initModel() {
        model = new DailyModel();
        model.register(this);
    }

    public void loadData(boolean isFirst){
        model.loadData(isFirst);
    }
}
