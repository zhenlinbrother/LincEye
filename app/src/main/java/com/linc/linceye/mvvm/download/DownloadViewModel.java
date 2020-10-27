package com.linc.linceye.mvvm.download;

import com.linc.download.bean.DownloadInfo;
import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;

import java.util.List;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/26
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadViewModel extends MvvmBaseViewModel<IDownloadView, DownloadModel> 
        implements IPagingModelListener<List<DownloadInfo>> {
    @Override
    public void onLoadFinish(BasePagingModel model, List<DownloadInfo> data, boolean isEmpty, boolean isFirstPage) {
        if (getPageView() != null){
            if (isEmpty) {
                if (isFirstPage) {
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
        if (getPageView() != null) {
            if (isFirstPage) {
                getPageView().showFailure(prompt);
            } else {
                getPageView().onLoadMoreFailure(prompt);
            }
        }
    }

    @Override
    public void initModel() {
        model = new DownloadModel();
        model.register(this);
    }

    public void loadData(boolean isFirst) {
        model.loadData(isFirst);
    }
}
