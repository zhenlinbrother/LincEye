package com.linc.linceye.mvvm.community.attention;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.community.attention.bean.AttentionCardBean;
import com.linc.linceye.mvvm.community.attention.bean.AttentionCardViewModel;
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
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AttentionModel<T> extends BasePagingModel<T> {

    private int start = 0;
    private int num = 10;
    private boolean newest = true;

    public void loadData(boolean isFirst){
        if (isFirst){
            start = 0;
        }

        RetrofitFactory
                .getApiService()
                .getAttentionData(start, num, newest)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<BaseCustomViewModel>>() {
                    @Override
                    public List<BaseCustomViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<BaseCustomViewModel> viewModels = new ArrayList<>();

                        AttentionCardBean attentionCardBean = GsonUtils.fromLocalJson(jsonObject.toString(), AttentionCardBean.class);
                        for (AttentionCardBean.ItemListBean itemListBean : attentionCardBean.getItemList()) {
                            AttentionCardViewModel cardViewModel = new AttentionCardViewModel();
                            cardViewModel.avatarUrl = itemListBean.getData().getHeader().getIcon();
                            cardViewModel.issuerName = itemListBean.getData().getHeader().getIssuerName();
                            cardViewModel.releaseTime = itemListBean.getData().getHeader().getTime();
                            cardViewModel.title = itemListBean.getData().getContent().getData().getTitle();
                            cardViewModel.description = itemListBean.getData().getContent().getData().getDescription();
                            cardViewModel.coverUrl = itemListBean.getData().getContent().getData().getCover().getFeed();
                            cardViewModel.playUrl = itemListBean.getData().getContent().getData().getPlayUrl();
                            cardViewModel.collectionCount = itemListBean.getData().getContent().getData().getConsumption().getCollectionCount();
                            cardViewModel.replyCount = itemListBean.getData().getContent().getData().getConsumption().getReplyCount();
                            cardViewModel.realCollectionCount = itemListBean.getData().getContent().getData().getConsumption().getRealCollectionCount();
                            cardViewModel.shareCount = itemListBean.getData().getContent().getData().getConsumption().getShareCount();
                            cardViewModel.category = itemListBean.getData().getContent().getData().getCategory();
                            cardViewModel.authorDescription = itemListBean.getData().getContent().getData().getAuthor().getDescription();
                            cardViewModel.blurredUrl = itemListBean.getData().getContent().getData().getCover().getBlurred();
                            cardViewModel.videoId = itemListBean.getData().getContent().getData().getId();
                            viewModels.add(cardViewModel);
                        }
                        return viewModels;
                    }
                })
                .subscribe(new RxSingleSubscriber<List<BaseCustomViewModel>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(code + message, isFirst);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<BaseCustomViewModel> value) {
                        loadSuccess((T)value, value.size() <= 0, isFirst);
                        start+=10;
                    }
                });
    }
}
