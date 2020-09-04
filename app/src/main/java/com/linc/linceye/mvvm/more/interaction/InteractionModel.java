package com.linc.linceye.mvvm.more.interaction;

import androidx.annotation.NonNull;

import com.linc.base.mvvm.view.IBasePagingView;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.mvvm.more.interaction.bean.TopicBean;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;

public class InteractionModel<T> extends BasePagingModel<T> {

    private int start = 1;
    private int num = 10;

    public void loadData(boolean isFirst){
        if (isFirst){
            start = 1;
        }
        RetrofitFactory
                .getApiService()
                .getInteractionData(start, num)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<ThemesItemViewModel>>() {
                    @Override
                    public List<ThemesItemViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<ThemesItemViewModel> viewModels = new ArrayList<>();

                        TopicBean topicBean = GsonUtils.fromLocalJson(jsonObject.toString(), TopicBean.class);
                        
                        if (topicBean != null){
                            for (TopicBean.ItemListBean itemData : topicBean.getItemList()) {
                                ThemesItemViewModel viewModel = new ThemesItemViewModel();
                                viewModel.coverUrl = itemData.getData().getImageUrl();
                                viewModel.title = itemData.getData().getTitle();
                                viewModel.description = itemData.getData().getViewCount()+" 人浏览 / "+itemData.getData().getJoinCount()+"人参与";
                                viewModels.add(viewModel);
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
