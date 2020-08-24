package com.linc.linceye.mvvm.home.nominate.bean.viewmodel;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


import com.linc.linceye.R;
import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateItemType;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-12
 */
public class FollowCardViewModel extends BaseCustomViewModel implements BindingAdapterItem {

    public String coverUrl;

    public int videoTime;

    public String authorUrl;

    public String description;

    public String title;

    public String video_description;

    public String userDescription;

    public String nickName;

    public String playerUrl;

    public String blurredUrl;

    public int videoId;

    // 点赞
    public int collectionCount;

    // 分享
    public int shareCount;


    @Override
    public int getViewType() {
        return R.layout.home_item_follow_card_view;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }


    public void setPlayerUrl(String playerUrl) {
        this.playerUrl = playerUrl;
    }

    public void setBlurredUrl(String blurredUrl) {
        this.blurredUrl = blurredUrl;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    @Override
    public int type() {
        return NominateItemType.FOLLOW_CARD_VIEW;
    }
}
