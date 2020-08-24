package com.linc.base.base.manager;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.linc.base.web.BaseWebViewFragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/21
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IWebViewConfig {

    /**
     * 获取白名单
     * @return
     */
    ArrayList<String> getWhiteList();

    /**
     * 获取WebView加载链接时头部参数
     * @return
     */
    Map<String, String> getHeader();

    /**
     * js回调Android
     * @param fragment
     * @param type
     * @param content
     */
    void jsCallBackApp(BaseWebViewFragment fragment, String type, String content);

    /**
     * @Description google建议，如果ssl需不需要通过，以弹框形式询问用户
     *  =============如果用户同意，调用{@link SslErrorHandler#proceed()}
     *  =============如果用户不同意，调用{@link SslErrorHandler#cancel()}
     * @param webView
     * @param handler
     * @param error
     */
    void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error);
}
