package com.linc.linceye.mvvm.more.interaction;

import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InteractionViewModel<T>
        extends MvvmBaseViewModel<IInteractionView, InteractionModel>
        implements IPagingModelListener<List<ThemesItemViewModel>> {
    @Override
    public void initModel() {
        model = new InteractionModel();
        model.register(this);
    }

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

    public void loadData(boolean isFirst){
        model.loadData(isFirst);
    }
}
