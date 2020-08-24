package com.linc.base.web;

import java.lang.ref.WeakReference;
/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/21
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class IWebViewJavaScriptInterface {

    protected WeakReference<BaseWebViewFragment> mFragment;

    public BaseWebViewFragment getWebViewFragment(){ return this.mFragment.get(); }

    public void setWebViewFragment(BaseWebViewFragment fragment){
        this.mFragment = new WeakReference<>(fragment);
    }
}
