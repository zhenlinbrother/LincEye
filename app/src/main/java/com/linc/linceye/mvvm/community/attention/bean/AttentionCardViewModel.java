package com.linc.linceye.mvvm.community.attention.bean;


import com.linc.linceye.base.viewmodel.BaseCustomViewModel;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AttentionCardViewModel extends BaseCustomViewModel
{
    public String avatarUrl;
    
    public String issuerName;
    
    public long releaseTime;
    
    public String title;
    
    public String description;
    
    public String coverUrl;

    public String blurredUrl;
    
    public String playUrl;
    
    public String category;
    
    public String authorDescription;

    public int videoId ;
    
    // 点赞
    public int collectionCount;
    
    // 分享
    public int shareCount;
    
    // 评论
    public int replyCount;
    
    // 收藏
    public int realCollectionCount;

    @Override
    public int type() {
        return 0;
    }
}
