package com.linc.download.jerry;

import android.content.Context;

import com.linc.download.config.DownloadConfig;
import com.linc.download.db.DownloadDB;
import com.linc.download.okHttp.DownloadOkHttpHelper;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.JerryDownloadGeneratedDatabaseHolder;


import okhttp3.OkHttpClient;

/**
 * <下载入口> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JerryDownloadConfig {

    public static void init(Context context) {
        FlowManager
                .init(FlowConfig.builder(context)
                        .addDatabaseConfig(
                                DatabaseConfig.builder(DownloadDB.class)
                                .databaseName(DownloadDB.NAME)
                                .build()
                        )
                        .build());

        FlowManager.initModule(JerryDownloadGeneratedDatabaseHolder.class);
    }

    /**
     * 设置OKhttp
     * @param okHttpClient
     */
    public static void setOkHttp(OkHttpClient okHttpClient) {
        DownloadOkHttpHelper.setOkHttpClient(okHttpClient);
    }

    /**
     * 设置下载的线程数
     * @param count 线程数
     */
    public static void setThreadCount(int count) {
        DownloadConfig.getInstance().setThreadCount(count);
    }

    /**
     * 设置下载路径
     * @param path 路径
     */
    public static void setDownloadFolder(String path) {
        DownloadConfig.getInstance().setDownloadFile(path);
    }
}
