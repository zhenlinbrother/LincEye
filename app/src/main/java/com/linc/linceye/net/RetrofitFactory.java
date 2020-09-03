package com.linc.linceye.net;

import com.linc.linceye.net.api.ApiService;
import com.linc.linceye.net.config.NetConfig;

/**
 * <创建retrofit -- 单例模式> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RetrofitFactory {

    private static final Object LOCK = new Object();

    private static volatile ApiService API_SERVICE;

    public static ApiService getApiService(){
        if (API_SERVICE == null){
            synchronized (LOCK){
                if (API_SERVICE == null){

                    API_SERVICE = new RetrofitHelper(NetConfig.BASE_URL, OkHttpHelper.getOkHttpClient())
                            .create(ApiService.class);
                }
            }
        }

        return API_SERVICE;
    }

    public static ApiService getApiService(String domain){

        if (API_SERVICE == null){
            synchronized (LOCK){
                if (API_SERVICE == null){
                    API_SERVICE = new RetrofitHelper(domain, OkHttpHelper.getOkHttpClient())
                            .create(ApiService.class);
                }
            }
        }
        return API_SERVICE;
    }

}
