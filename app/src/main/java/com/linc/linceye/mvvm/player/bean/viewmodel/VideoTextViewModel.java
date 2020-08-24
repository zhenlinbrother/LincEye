package com.linc.linceye.mvvm.player.bean.viewmodel;

import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.player.adapter.IVideoItemType;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-21
 */
public class VideoTextViewModel extends BaseCustomViewModel {
    public String textTitle;

    @Override
    public int type() {
        return IVideoItemType.TITLE_VIEW;
    }
}
