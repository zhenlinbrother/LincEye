package com.linc.linceye.mvvm.community.recommend;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.community.recommend.bean.CommunityColumnsCard;
import com.linc.linceye.mvvm.community.recommend.bean.HorizontalScrollCard;
import com.linc.linceye.mvvm.community.recommend.bean.viewmodel.CloumnsCardViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/24
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RecommendModel<T> extends BasePagingModel<T> {

    private String startScore;
    private int pageCount = 2;

    public void loadData(boolean isFirst){
        if (isFirst){
            startScore = "";
            pageCount = 2;
        }
        RetrofitFactory
                .getApiService()
                .getCommunityRecommend(startScore, pageCount)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<BaseCustomViewModel>>() {
                    @Override
                    public List<BaseCustomViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<BaseCustomViewModel> viewModels = new ArrayList<>();

                        String nextPageUrl = jsonObject.optString("nextPageUrl", "");
                        startScore = nextPageUrl.substring(nextPageUrl.indexOf("startScore=") + 11, nextPageUrl.indexOf("&"));
                        pageCount = Integer.parseInt(nextPageUrl.substring(nextPageUrl.indexOf("&") + 11));

                        JSONArray itemList = jsonObject.optJSONArray("itemList");
                        if (itemList != null) {
                            for (int i = 0; i < itemList.length(); i++) {
                                JSONObject ccurrentObject = itemList.getJSONObject(i);
                                switch (ccurrentObject.optString("type")) {
                                    case "horizontalScrollCard":
                                        HorizontalScrollCard scrollCard = GsonUtils
                                                .fromLocalJson(ccurrentObject.toString(),
                                                        HorizontalScrollCard.class);
                                        viewModels.add(scrollCard);
                                        break;
                                    case "communityColumnsCard":
                                        CommunityColumnsCard communityColumnsCard =
                                                GsonUtils.fromLocalJson(
                                                        ccurrentObject.toString(),
                                                        CommunityColumnsCard.class);
                                        parseCard(viewModels, communityColumnsCard);
                                        break;
                                    default:
                                        break;
                                }
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
                        loadSuccess((T)value, value.size() <= 0, isFirst);
                    }
                });
    }

    private void parseCard(List<BaseCustomViewModel> viewModels,
                           CommunityColumnsCard columnsCard)
    {
        CloumnsCardViewModel cardViewModel = new CloumnsCardViewModel();
        if (columnsCard != null)
        {
            cardViewModel.coverUrl = columnsCard.getData()
                    .getContent()
                    .getData()
                    .getCover()
                    .getFeed();
            cardViewModel.description =
                    columnsCard.getData().getContent().getData().getDescription();
            cardViewModel.nickName = columnsCard.getData()
                    .getContent()
                    .getData()
                    .getOwner()
                    .getNickname();
            cardViewModel.avatarUrl = columnsCard.getData()
                    .getContent()
                    .getData()
                    .getOwner()
                    .getAvatar();
            cardViewModel.collectionCount = columnsCard.getData()
                    .getContent()
                    .getData()
                    .getConsumption()
                    .getCollectionCount();
            cardViewModel.imgWidth =
                    columnsCard.getData().getContent().getData().getWidth();
            cardViewModel.imgHeight =
                    columnsCard.getData().getContent().getData().getHeight();
            viewModels.add(cardViewModel);
        }

    }
}
