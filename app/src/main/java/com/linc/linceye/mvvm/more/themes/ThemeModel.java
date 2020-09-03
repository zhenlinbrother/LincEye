package com.linc.linceye.mvvm.more.themes;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BaseModel;
import com.linc.linceye.bean.RespData;
import com.linc.linceye.mvvm.more.themes.bean.TabInfo;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONObject;

/**
 * <通知-主题> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemeModel<T> extends BaseModel<T> {

    public void load(){
        RetrofitFactory
                .getApiService()
                .getTabData()
                .compose(RxParser.handleSingleToResult1())
                .subscribe(new RxSingleSubscriber<JSONObject>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(code + message, true);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull JSONObject value) {
                        TabInfo tabInfo = GsonUtils.fromLocalJson(value.toString(), TabInfo.class);

                        loadSuccess((T)tabInfo.getTabInfo().getTabList(),
                                tabInfo.getTabInfo().getTabList().size() <= 0,
                                true);
                    }
                });
    }
}
