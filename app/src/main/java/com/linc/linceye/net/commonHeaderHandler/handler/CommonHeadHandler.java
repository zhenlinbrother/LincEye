package com.linc.linceye.net.commonHeaderHandler.handler;

import java.util.Map;

import okhttp3.Request;

/**
 * <通用参数请求头> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonHeadHandler extends BaseHandler{
    @Override
    protected boolean isHandler(Request request, Map<String, String> headParams) {
        return true;
    }

    @Override
    protected void handle(Request request, Map<String, String> headParams) {

    }
}
