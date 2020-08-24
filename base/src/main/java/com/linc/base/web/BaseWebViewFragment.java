package com.linc.base.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.linc.base.base.manager.BitWebViewManger;

import java.util.List;

/**
 * <WebView> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/21
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class BaseWebViewFragment extends Fragment implements ProgressWebView.OnStateListener{

    private static String TAG = BaseWebViewFragment.class.getSimpleName();

    protected ProgressWebView mWebView;

    protected String mUrl;

    private WebListener mWebListener;

    public static BaseWebViewFragment newInstance(String url){
        BaseWebViewFragment fragment = new BaseWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LOAD_URL", url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null){
            mUrl = arguments.getString("LOAD_URL");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null || container.getContext() == null){
            return null;
        }

        mWebView = new ProgressWebView(container.getContext());
        mWebView.addOnStateListener(this);
        return mWebView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView(mWebView);
        initWebViewSetting(mWebView.getSettings());

        mWebView.loadUrl(this.mUrl, BitWebViewManger.getInstance().getWebViewConfig().getHeader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * 初始化webview
     * @param webView
     */
    private void initWebView(ProgressWebView webView) {

        //第三方的cookie
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }

        webView.setWebViewClient(new BaseWebViewClient());

        webView.setLongClickable(true);
    }

    private void initWebViewSetting(WebSettings settings) {

        //支持js脚本
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        //支持缩放
        settings.setBuiltInZoomControls(true);
        //去除缩放按钮
        settings.setDisplayZoomControls(false);

        //扩大比例的缩放
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);

        //多窗口
        settings.supportMultipleWindows();
        //关闭webview中缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持通过JS打开新窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);

        //启用地理定位
//        settings.setGeolocationEnabled(true);
        //设置渲染优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        // 设置支持本地存储
        settings.setDatabaseEnabled(true);
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);

        addJavascriptInterface();

    }

    @SuppressLint("AddJavascriptInterface")
    protected void addJavascriptInterface() {
    }

    //处理WebView中回退的事件
    public boolean onConsumeBackEvent(FragmentManager fragmentManager) {
        WebBackForwardList webBackForwardList = this.mWebView.copyBackForwardList();

        //如果有回退历史，而且可回退，就消费该事件
        if (webBackForwardList.getSize() > 0 && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        boolean consume = isConsumeBackEvent(getChildFragmentManager());
        if (consume) {
            // 子fragment消费成功以后，判断当前fragment是否还有子fragment，没有的话，看情况决定是否关闭
            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                return fragmentManager.popBackStackImmediate();
            }
            return true;
        } else {
            // 子fragment未消费回退事件,则由当前fragment进行消费
            return fragmentManager.popBackStackImmediate();
        }
    }

    private boolean isConsumeBackEvent(FragmentManager childFragmentManager) {
        if (childFragmentManager == null) {
            return false;
        }

        List<Fragment> fragments = childFragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            int size = fragments.size();

            for (int i = size - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof BaseWebViewFragment) {
                    BaseWebViewFragment baseFragment = (BaseWebViewFragment) fragment;
                    boolean consume = baseFragment.onConsumeBackEvent(childFragmentManager);

                    if (consume) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }

    @Override
    public void onChange(WebView webView, int newProgress) {

    }

    @Override
    public void onReceivedTiTle(String title) {
        if (mWebListener == null) {
            return;
        }

        mWebListener.onReceivedTitle(title);
    }

    @Override
    public void onReceivedIcon(Bitmap icon, int color) {
        if (mWebListener == null) {
            return;
        }
        mWebListener.onReceivedIcon(icon, color);
    }

    private class BaseWebViewClient extends WebViewClient{
        //https的ssl验证，需要弹窗询问

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            /**
             * Google建议，如果ssl不需要通过，以弹窗形式询问
             * 如果用户同意，调用{@link SslErrorHandler#proceed()}
             * 如果用户不同意，调用{@link SslErrorHandler#cancel()}
             */
            BitWebViewManger
                    .getInstance()
                    .getWebViewConfig()
                    .onReceivedSslError(view, handler, error);
        }
    }

    /**
     * 回调监听器
     */
    public interface WebListener{
        /**
         * 标题获取完后回调
         * @param title
         */
        void onReceivedTitle(String title);

        /**
         * 图标获取完回调
         * @param icon
         * @param color
         */
        void onReceivedIcon(Bitmap icon, int color);
    }
}
