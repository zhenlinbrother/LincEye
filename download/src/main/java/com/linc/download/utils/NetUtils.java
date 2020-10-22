package com.linc.download.utils;

import com.linc.download.bean.DownloadInfo;
import com.linc.download.okHttp.DownloadOkHttpHelper;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class NetUtils {

    /**
     * 进行网络请求
     * @param downloadInfo
     * @param headers
     * @return
     * @throws IOException
     */
    public static Call request(DownloadInfo downloadInfo,
                               Map<String, String> headers) throws IOException {
        //进行网络请求
        Request.Builder builder = new Request.Builder();
        builder.url(downloadInfo.getUrl());

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return DownloadOkHttpHelper.getOkHttpInstance()
                .newCall(builder.build());
    }
}
