package com.linc.linceye.net;

import com.linc.linceye.net.connverter.JsonConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RetrofitHelper {

    private Retrofit mRetrofit;

    //域名
    private String domain;

    public RetrofitHelper(String domain, OkHttpClient okHttpClient){
        this.domain = domain;

        this.mRetrofit = new Retrofit.Builder()
                .baseUrl(domain)
                .client(okHttpClient)
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    public <T> T create(Class<? extends T> clazz){
        return this.mRetrofit.create(clazz);
    }
}
