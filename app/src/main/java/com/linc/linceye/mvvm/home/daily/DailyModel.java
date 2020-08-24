package com.linc.linceye.mvvm.home.daily;

import android.util.Log;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.FollowCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.TextCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.FollowCardViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.SingleTitleViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;

public class DailyModel<T> extends BasePagingModel<T> {

    private String date = "";
    private int num = 2;

    public void loadData(boolean isFirst){
        RetrofitFactory
                .getApiService()
                .getDailyData(date, num)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<BaseCustomViewModel>>() {
                    @Override
                    public List<BaseCustomViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<BaseCustomViewModel> viewModels = new ArrayList<>();
                        JSONArray itemList = jsonObject.optJSONArray("itemList");

                        String nextPageUrl = jsonObject.optString("nextPageUrl", "");
                        int dateStart = nextPageUrl.indexOf("date=") + 5;
                        date = nextPageUrl.substring(dateStart, nextPageUrl.indexOf("&"));
                        num = Integer.parseInt(nextPageUrl.substring(nextPageUrl.indexOf("&")+ 5));

                        if (itemList == null){
                            return null;
                        }

                        for (int i = 0; i < itemList.length(); i++){
                            JSONObject currentObject = itemList.getJSONObject(i);
                            switch (currentObject.optString("type")){
                                case "textCard":
                                    TextCardBean textCardBean = GsonUtils.fromLocalJson(
                                            currentObject.toString(),
                                            TextCardBean.class);
                                    if (textCardBean.getData().getText().equals("今日社区精选")){
                                        break;
                                    }
                                    SingleTitleViewModel viewModel =
                                            new SingleTitleViewModel();
                                    viewModel.title = textCardBean.getData().getText();
                                    viewModels.add(viewModel);
                                    break;
                                case "followCard":
                                    FollowCardBean followCardBean = GsonUtils
                                            .fromLocalJson(currentObject.toString(),
                                                    FollowCardBean.class);
                                    paresFollowCard(viewModels, followCardBean);
                            }
                        }
                        return viewModels;
                    }
                })
                .subscribe(new RxSingleSubscriber<List<BaseCustomViewModel>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(message, isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<BaseCustomViewModel> value) {
                        loadSuccess((T) value, value.size() <= 0, isFirst);
                    }
                });
    }

    private void paresFollowCard(List<BaseCustomViewModel> viewModels,
                                 FollowCardBean cardBean)
    {
        FollowCardViewModel followCardViewModel = new FollowCardViewModel();
        followCardViewModel.coverUrl =
                cardBean.getData().getContent().getData().getCover().getDetail();
        followCardViewModel.videoTime =
                cardBean.getData().getContent().getData().getDuration();
        followCardViewModel.authorUrl =
                cardBean.getData().getContent().getData().getAuthor().getIcon();
        followCardViewModel.description =
                cardBean.getData().getContent().getData().getAuthor().getName()
                        + " / #"
                        + cardBean.getData().getContent().getData().getCategory();
        followCardViewModel.title =
                cardBean.getData().getContent().getData().getTitle();
        followCardViewModel.video_description = cardBean.getData().getContent().getData().getDescription();
        followCardViewModel.userDescription = cardBean.getData().getContent().getData().getAuthor().getDescription();
        followCardViewModel.playerUrl = cardBean.getData().getContent().getData().getPlayUrl();
        followCardViewModel.blurredUrl = cardBean.getData().getContent().getData().getCover().getBlurred();
        followCardViewModel.videoId = cardBean.getData().getContent().getData().getId();
        viewModels.add(followCardViewModel);
    }
}
