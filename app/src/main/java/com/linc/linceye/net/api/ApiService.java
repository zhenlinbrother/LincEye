package com.linc.linceye.net.api;

import com.linc.linceye.bean.RespData;
import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;


import org.json.JSONObject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.GET;
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

    @GET("api/v4/video/related")
    Observable<JSONObject> getRecommendData(@Query("id") int id);

    @GET("api/v2/replies/video")
    Observable<JSONObject> getRetryData(@Query("videoId") int videoId);
}
