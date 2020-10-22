package com.linc.download.okHttp;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * <获取 OkHttp 实例> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadOkHttpHelper {

    private volatile static OkHttpClient OK_HTTP_CLIENT;

    private static final Object LOCK = new Object();

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        synchronized (LOCK) {
            OK_HTTP_CLIENT = okHttpClient;
        }
    }

    public static OkHttpClient getOkHttpInstance() {

        if (OK_HTTP_CLIENT == null) {
            synchronized (LOCK) {
                if (OK_HTTP_CLIENT == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    OK_HTTP_CLIENT = builder
                            .connectTimeout(DownloadOkHttpConfig.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                            .readTimeout(DownloadOkHttpConfig.READ_TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(DownloadOkHttpConfig.WRITE_TIME_OUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }

        return OK_HTTP_CLIENT;
    }
}
