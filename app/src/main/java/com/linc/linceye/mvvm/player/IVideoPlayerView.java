package com.linc.linceye.mvvm.player;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

import java.util.List;

public interface IVideoPlayerView extends IBasePagingView {

    /**
     * 数据加载成功
     * @param viewModels
     */
    void onDataLoadFinish(List<BaseCustomViewModel> viewModels);
}
