package com.linc.base.base.manager;

import android.net.http.SslError;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.linc.base.web.BaseWebViewFragment;
import com.linc.base.web.IWebViewJavaScriptInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/21
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BitWebViewManger {

    private static final BitWebViewManger sInstance = new BitWebViewManger();

    public static BitWebViewManger getInstance(){ return sInstance; }

    //WebView的配置
    private IWebViewConfig mWebViewConfig;

    private Class<? extends IWebViewJavaScriptInterface> mJavaScriptInterfaceClazz;

    private BitWebViewManger(){
        this.mWebViewConfig = new DefaultWebViewConfig();
        this.mJavaScriptInterfaceClazz = JDefaultJavaScriptInterface.class;
    }

    public IWebViewConfig getWebViewConfig(){ return this.mWebViewConfig; }

    public void setWebViewConfig(IWebViewConfig webViewConfig){
        this.mWebViewConfig = webViewConfig;
    }

    public Class<? extends IWebViewJavaScriptInterface> getJavaScriptInterfaceClazz() {
        return this.mJavaScriptInterfaceClazz;
    }

    public void setJavaScriptInterfaceClazz(Class<? extends IWebViewJavaScriptInterface> javaScriptInterfaceClazz) {
        this.mJavaScriptInterfaceClazz = javaScriptInterfaceClazz;
    }

    public static class DefaultWebViewConfig implements IWebViewConfig{

        private static final String TAG = DefaultWebViewConfig.class.getSimpleName();

        @Override
        public ArrayList<String> getWhiteList() {
            return new ArrayList<>();
        }

        @Override
        public Map<String, String> getHeader() {
            return new HashMap<>();
        }

        @Override
        public void jsCallBackApp(BaseWebViewFragment fragment, String type, String content) {
            Log.i(TAG, "jsCallBackApp: type" + type + "; content" + content);
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler handler, SslError error) {

        }
    }

    public static class JDefaultJavaScriptInterface extends IWebViewJavaScriptInterface {

        @JavascriptInterface
        public void jsCallApp(String type, String content) {
            BitWebViewManger.getInstance().getWebViewConfig().jsCallBackApp(super.mFragment.get(), type, content);
        }

    }
}
