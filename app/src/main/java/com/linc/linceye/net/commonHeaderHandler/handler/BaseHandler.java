package com.linc.linceye.net.commonHeaderHandler.handler;

import java.util.Map;

import okhttp3.Request;

/**
 * <责任链处理基类> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseHandler {

    //下一个处理者
    private BaseHandler nextHandler;

    /**
     * 获取对应的头签名参数
     * @param request
     * @param headParams
     */
    public final void getHeaderParams(Request request, Map<String, String> headParams){

        if (isHandler(request, headParams)){
            handle(request, headParams);
        }

        if (nextHandler != null){
            nextHandler.getHeaderParams(request, headParams);
        }
    }

    /**
     * 获取当前处理者是否要进行处理
     * @param request
     * @param headParams
     * @return
     */
    protected abstract boolean isHandler(Request request, Map<String, String> headParams);

    /**
     * 当前处理的逻辑
     * @param request
     * @param headParams
     */
    protected abstract void handle(Request request, Map<String, String> headParams);
}
