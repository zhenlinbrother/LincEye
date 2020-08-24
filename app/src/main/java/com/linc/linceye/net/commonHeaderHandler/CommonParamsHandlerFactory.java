package com.linc.linceye.net.commonHeaderHandler;

import com.linc.linceye.net.commonHeaderHandler.handler.BaseHandler;
import com.linc.linceye.net.commonHeaderHandler.handler.CommonHeadHandler;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/12
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonParamsHandlerFactory {

    /**
     * 获取处理链
     * @return
     */
    public static BaseHandler getHandler(){

        return new CommonHeadHandler();
    }
}
