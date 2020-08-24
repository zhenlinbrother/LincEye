package com.linc.linceye.net;

import com.linc.linceye.net.config.NetConfig;
import com.linc.linceye.net.interceptor.CommonParamsInterceptor;
import com.linc.linceye.net.interceptor.LogInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <获取OkHttp实例> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class OkHttpHelper {

    private volatile static OkHttpClient OK_HTTP_CLIENT;

    private static final Object LOCK = new Object();

    public static OkHttpClient getOkHttpClient(){

        if (OK_HTTP_CLIENT == null){
            synchronized (LOCK){
                if (OK_HTTP_CLIENT == null){
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    OK_HTTP_CLIENT = builder
                            .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(NetConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(NetConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(new CommonParamsInterceptor())
                            .addInterceptor(new LogInterceptor())
                            .addInterceptor(new Interceptor() {
                                @NotNull
                                @Override
                                public Response intercept(@NotNull Chain chain) throws IOException {
                                    Request request = chain.request()
                                            .newBuilder()
                                            .removeHeader("User-Agent")
                                            .addHeader("User-Agent", "android")
                                            .build();
                                    return chain.proceed(request);
                                }
                            })
                            .build();

                }
            }
        }

        return OK_HTTP_CLIENT;
    }
}
