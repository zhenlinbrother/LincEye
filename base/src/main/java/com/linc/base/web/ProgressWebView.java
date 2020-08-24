package com.linc.base.web;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.core.content.res.ResourcesCompat;
import androidx.palette.graphics.Palette;

import com.linc.base.R;
import com.linc.base.base.utils.UIUtils;

/**
 * <带进度条的webview> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/21
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ProgressWebView extends WebView {

    private ProgressBar mProgressBar;
    private OnStateListener mListener;

    public ProgressWebView(Context context){
        super(context);
        initView();
    }

    private void initView() {
        mProgressBar = new ProgressBar(
                getContext(),
                null,
                android.R.attr.progressBarStyleHorizontal
        );
        mProgressBar.setProgressDrawable(ResourcesCompat
                .getDrawable(getResources(),
                        R.drawable.j_process_horizontal,
                        null));
        mProgressBar.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        UIUtils.dip2px(getContext(), 3),
                        0,
                        0)
        );

        addView(mProgressBar);
        setWebChromeClient(new WebChromeClient());
    }

    public void addOnStateListener(OnStateListener listener){
        this.mListener = listener;
    }

    private void onChange(WebView webView, int newProgress){
        if (mListener != null){
            mListener.onChange(webView, newProgress);
        }
    }

    private class WebChromeClient extends android.webkit.WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            onChange(view, newProgress);

            if (newProgress == 100){
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE)
                    mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mListener == null){
                return;
            }
            mListener.onReceivedTiTle(title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);

            if (mListener == null){
                return;
            }

            Palette.from(icon)
                    .generate(palette -> {
                        Palette.Swatch vibrant = palette.getVibrantSwatch();

                        if (vibrant == null){
                            for (Palette.Swatch swatch : palette.getSwatches()){
                                if (swatch == null){
                                    continue;
                                }
                                vibrant = swatch;
                                break;
                            }
                        }

                        if (vibrant == null){
                            return;
                        }
                        int rbg = vibrant.getRgb();

                        mListener.onReceivedIcon(icon, changeColor(rbg));
                    });
        }
    }

    private int changeColor(int rgb){
        int red = rgb >> 16 & 0xFF;
        int green = rgb >> 8 & 0xFF;
        int blue = rgb & 0xFF;
        red = (int) Math.floor(red * (1 - 0.2));
        green = (int) Math.floor(green * (1 - 0.2));
        blue = (int) Math.floor(blue * (1 - 0.2));
        return Color.rgb(red, green, blue);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 状态回调
     */
    public interface OnStateListener{
        /**
         * 进度条变化
         * @param webView
         * @param newProgress
         */
        void onChange(WebView webView, int newProgress);

        /**
         * 网页标题
         * @param title
         */
        void onReceivedTiTle(String title);

        /**
         * 网页图标
         * @param icon
         * @param color
         */
        void onReceivedIcon(Bitmap icon, int color);
    }
}
