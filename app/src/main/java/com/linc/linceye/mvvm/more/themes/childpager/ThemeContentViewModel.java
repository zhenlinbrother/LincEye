package com.linc.linceye.mvvm.more.themes.childpager;

import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

public class ThemeContentViewModel extends MvvmBaseViewModel<IThemeContentView, ThemeContentModel>
        implements IPagingModelListener<List<ThemesItemViewModel>> {
    @Override
    public void onLoadFinish(BasePagingModel model, List<ThemesItemViewModel> data, boolean isEmpty, boolean isFirstPage) {
        if (getPageView() != null){
            if (isEmpty){

                if (isFirstPage){
                    getPageView().showEmpty();
                } else {
                    getPageView().onLoadMoreEmpty();
                }
            } else {
                getPageView().onDataLoaded(data, isFirstPage);
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
        model = new ThemeContentModel();
        model.register(this);
    }

    public void loadData(int position, String url, boolean isFirst){
        model.loadData(position, url, isFirst);
    }
}
