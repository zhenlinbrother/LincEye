package com.linc.linceye.mvvm.player;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.listener.IPagingModelListener;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.base.viewmodel.MvvmBaseViewModel;

import java.util.List;

public class VideoPlayerViewModel extends MvvmBaseViewModel<IVideoPlayerView, VideoPlayerModel>
        implements IPagingModelListener<List<BaseCustomViewModel>> {
    @Override
    public void onLoadFinish(BasePagingModel model, List<BaseCustomViewModel> data, boolean isEmpty, boolean isFirstPage) {
        if (getPageView() != null){
            if (data != null && data.size() > 0){
                getPageView().onDataLoadFinish(data);
            } else {
                getPageView().showEmpty();
            }
        }
    }

    @Override
    public void onLoadFail(BasePagingModel model, String prompt, boolean isFirstPage) {
        if (getPageView() != null){
            getPageView().showFailure(prompt);
        }
    }

    @Override
    public void initModel() {
        model = new VideoPlayerModel();
        model.register(this);
    }

    public void loadData(boolean isFirst, int videoId){
        model.loadData(isFirst, videoId);
    }

}
