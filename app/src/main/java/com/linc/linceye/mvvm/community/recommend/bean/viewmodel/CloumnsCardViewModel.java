package com.linc.linceye.mvvm.community.recommend.bean.viewmodel;


import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

/**
 * 应用模块:
 * <p>
 * 类描述:
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-17
 */
public class CloumnsCardViewModel extends BaseCustomViewModel {
    public String coverUrl;
    public String description;
    public String avatarUrl;
    public String nickName;
    public int collectionCount;
    public int imgWidth;
    public int imgHeight;

    @Override
    public int type() {
        return IRecommendItemType.COMMUNITY_CARD_VIEW;
    }
}
