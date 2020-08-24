package com.linc.linceye.net.interceptor;

import com.linc.linceye.net.commonHeaderHandler.CommonParamsHandlerFactory;
import com.linc.linceye.net.commonHeaderHandler.handler.BaseHandler;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * <通用参数拦截器> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonParamsInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        rebuildHeader(request, requestBuilder);

        request = requestBuilder.build();

        return chain.proceed(request);
    }

    private void rebuildHeader(Request oldRequest, Request.Builder requestBuilder){

        if (oldRequest == null){
            return;
        }

        Headers.Builder headerBuilder = oldRequest.headers().newBuilder();

        //获取头参数处理链
        Map<String, String> headParams = new HashMap<>();
        BaseHandler handler = CommonParamsHandlerFactory.getHandler();
        handler.getHeaderParams(oldRequest, headParams);

        for (Map.Entry<String, String> entry : headParams.entrySet()){
            headerBuilder.add(entry.getKey(), entry.getValue());
        }
        requestBuilder.headers(headerBuilder.build());
    }
}
