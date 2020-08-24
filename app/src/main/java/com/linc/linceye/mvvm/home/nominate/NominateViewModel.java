package com.linc.linceye.mvvm.home.nominate;
import android.util.Log;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;
import java.util.ArrayList;

public class NominateViewModel extends MvvmBaseViewModel<INominateView, NominateModel<ItemListBean>>
        implements IPagingModelListener<ArrayList<BaseCustomViewModel>> {

    @Override
    public void initModel() {
        model = new NominateModel(getPageView());
        model.register(this);
    }

    public NominateViewModel(){
        initModel();
    }

    public void loadData(boolean isFirst){
        model.loadData(isFirst);
    }

    @Override
    public void onLoadFinish(BasePagingModel model, ArrayList<BaseCustomViewModel> data, boolean isEmpty, boolean isFirstPage) {
        Log.d("fwerwerwr", "onLoadFinish: " + isFirstPage);
        if (getPageView() != null){

            if (isEmpty){

                if (isFirstPage){
                    getPageView().showEmpty();
                } else {
                    getPageView().onLoadMoreEmpty();
                }
            } else {
                getPageView().showContent();
                getPageView().onDataLoadFinish(data, isFirstPage);
            }
        }
    }

    @Override
    public void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage) {
        if (getPageView() != null)
        {
            if (isFirstPage)
            {
                // 刷新失败
                getPageView().showFailure(prompt);
            }
            else
            {
                // 加载更多失败
                getPageView().onLoadMoreFailure(prompt);

            }
        }
    }
}
