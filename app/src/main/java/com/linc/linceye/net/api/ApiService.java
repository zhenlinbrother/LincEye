package com.linc.linceye.net.api;

import com.linc.linceye.bean.RespData;
import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;
import com.linc.linceye.mvvm.more.themes.bean.TabInfo;


import org.json.JSONObject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * <api> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ApiService {

    /**
     * 首页推荐
     * @param page
     * @param isTag
     * @param adIndex
     * @return
     */
    @GET("api/v5/index/tab/allRec")
    Single<JSONObject> getData(@Query("page") int page,
                               @Query("isTag") boolean isTag,
                               @Query("adIndex") int adIndex);

    /**
     * 首页发现
     * @return
     */
    @GET("api/v7/index/tab/discovery?udid=fa53872206ed42e3857755c2756ab683fc22d64a&vc=591&vn=6.2.1&size=720X1280&deviceModel=Che1-CL20&first_channel=eyepetizer_zhihuiyun_market&last_channel=eyepetizer_zhihuiyun_market&system_version_code=19")
    Single<JSONObject> getDiscoverData();

    /**
     * 首页日常
     * @param date
     * @param num
     * @return
     */
    @GET("api/v5/index/tab/feed")
    Single<JSONObject> getDailyData(@Query("date") String date,
                                    @Query("num") int num);

    /**
     * 视频-推荐
     * @param id
     * @return
     */
    @GET("api/v4/video/related")
    Observable<JSONObject> getRecommendData(@Query("id") int id);

    /**
     * 视频-回复
     * @param videoId
     * @return
     */
    @GET("api/v2/replies/video")
    Observable<JSONObject> getRetryData(@Query("videoId") int videoId);

    /**
     * 社区-推荐
     * @param startScore
     * @param pageCount
     * @return
     */
    @GET("api/v7/community/tab/rec")
    Single<JSONObject> getCommunityRecommend(@Query("startScore") String startScore,
                                             @Query("pageCount") int pageCount);

    /**
     * 社区-关注
     * @param start
     * @param num
     * @param newest
     * @return
     */
    @GET("api/v6/community/tab/follow")
    Single<JSONObject> getAttentionData(@Query("start") int start,
                                        @Query("num") int num,
                                        @Query("newest") boolean newest);

    /**
     * 通知-主题 tab数据
     * @return
     */
    @GET("api/v7/tag/tabList")
    Single<JSONObject> getTabData();


    /**
     * 通知-主题 子页面数据
     * @param position 位置
     * @param start 开始
     * @param num 请求数量
     * @return
     */
    @GET("api/v7/tag/childTab/{id}")
    Single<JSONObject> getChildThemeContent(@Path ("id") int position,
                                            @Query("start") int start,
                                            @Query("num") int num);

    /**
     * 通知-推送
     * @param vc
     * @param deviceModel
     * @return
     */
    @GET("api/v3/messages")
    Single<JSONObject> getPushData(@Query("vc") String vc,
                                   @Query("deviceModel") String deviceModel,
                                   @Query("start") int start,
                                   @Query("num") int num);

    @GET("api/v7/topic/list")
    Single<JSONObject> getInteractionData(@Query("start") int start,
                                          @Query("num") int num);
}
