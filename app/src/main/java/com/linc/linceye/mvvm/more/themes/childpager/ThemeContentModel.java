package com.linc.linceye.mvvm.more.themes.childpager;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesContent;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;

public class ThemeContentModel<T> extends BasePagingModel<T> {

    private int start = 1;
    private int num = 20;
    private String nextPageUrl;

    public void loadData(int position, String apiUrl, boolean isFirst){

        if (!isFirst){
            //如果不是第一次，没有数据则为空
            if (TextUtils.isEmpty(nextPageUrl)){
                loadSuccess(null, true, false);
                return;
            }
        }

        RetrofitFactory
                .getApiService()
                .getChildThemeContent(position, start, num)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<ThemesItemViewModel>>() {
                    @Override
                    public List<ThemesItemViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<ThemesItemViewModel> viewModels = new ArrayList<>();

                        ThemesContent themesContent = GsonUtils.fromLocalJson(jsonObject.toString(), ThemesContent.class);

                        if (themesContent != null){
                            nextPageUrl = themesContent.getNextPageUrl();
                            for (ThemesContent.ItemListBean itemListBean : themesContent.getItemList()) {
                                ThemesItemViewModel itemViewModel = new ThemesItemViewModel();
                                itemViewModel.coverUrl = itemListBean.getData().getIcon();
                                itemViewModel.title = itemListBean.getData().getTitle();
                                itemViewModel.description =
                                        itemListBean.getData().getDescription();
                                viewModels.add(itemViewModel);
                            }
                        }
                        return viewModels;
                    }
                })
                .subscribe(new RxSingleSubscriber<List<ThemesItemViewModel>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(code + message, isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<ThemesItemViewModel> value) {
                        loadSuccess((T)value, value.size() <= 0, isFirst);
                    }
                });
    }
}
