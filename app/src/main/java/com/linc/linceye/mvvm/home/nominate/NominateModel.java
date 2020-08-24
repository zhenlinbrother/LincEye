package com.linc.linceye.mvvm.home.nominate;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.bean.RespData;
import com.linc.linceye.mvvm.home.nominate.bean.CustomerBean;
import com.linc.linceye.mvvm.home.nominate.bean.FollowCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;
import com.linc.linceye.mvvm.home.nominate.bean.SquareCardCollectionBean;
import com.linc.linceye.mvvm.home.nominate.bean.TextCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.VideoSmallCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.BannerCardViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.FollowCardViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.SingleTitleViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.TitleViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.VideoCardViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NominateModel<T> extends BasePagingModel<T> {

    List<BaseCustomViewModel> viewModels = new ArrayList<>();

    private int page = 0;
    private boolean isTag = true;
    private int adIndex = 3;

    public NominateModel(INominateView view) {

    }

    public void loadData(boolean isFirst){
        RetrofitFactory
                .getApiService()
                .getData(page, isTag, adIndex)
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<BaseCustomViewModel>>() {

                    @Override
                    public List<BaseCustomViewModel> apply(JSONObject responseBody) throws Throwable {
                        JSONArray itemList = responseBody.optJSONArray("itemList");
                        if (itemList != null) {
                            for (int i = 0; i < itemList.length(); i++) {
                                JSONObject currentObject = itemList.getJSONObject(i);
                                switch (currentObject.optString("type")) {
                                    case "squareCardCollection":
                                        SquareCardCollectionBean squareCardCollectionBean = GsonUtils.fromLocalJson(currentObject.toString(),
                                                SquareCardCollectionBean.class);
                                        parseCollectionCard(viewModels, squareCardCollectionBean);
                                        break;
                                case "textCard":
                                    TextCardBean textCardBean = GsonUtils.fromLocalJson(currentObject.toString(),
                                            TextCardBean.class);
                                    SingleTitleViewModel singleTitleViewModel = new SingleTitleViewModel();
                                    singleTitleViewModel.title = textCardBean.getData().getText();
                                    viewModels.add(singleTitleViewModel);
                                    break;
                                case "videoSmallCard":
                                    VideoSmallCardBean videoSmallCardBean = GsonUtils.fromLocalJson(currentObject.toString(),
                                            VideoSmallCardBean.class);
                                    parseVideoCard(viewModels, videoSmallCardBean);
                                    break;
                                case "followCard":
                                    FollowCardBean followCardBean = GsonUtils.fromLocalJson(currentObject.toString(),
                                            FollowCardBean.class);
                                    parseFollowCard(viewModels, followCardBean);
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
                        page += 1;
                        adIndex += 2;
                        loadSuccess((T) viewModels, viewModels.size() <= 0, isFirst);
                    }
//
//                    @Override
//                    protected void onSuccessRes(@NonNull RespData<ItemListBean> value) {
//                        Log.d("fwerwe", "onSuccessRes: " + value.toString());
//                        loadSuccess((T) viewModels, false, true);
//                    }
                });
    }

    private void parseCollectionCard(List<BaseCustomViewModel> viewModels,
                                     SquareCardCollectionBean squareCardCollectionBean){

        if (squareCardCollectionBean == null){
            return;
        }
        TitleViewModel titleViewModel = new TitleViewModel();
        titleViewModel.title = squareCardCollectionBean.getData().getHeader().getTitle();
        titleViewModel.actionTitle = squareCardCollectionBean.getData().getHeader().getRightText();
        viewModels.add(titleViewModel);
        //解析精选视频
        for (int i = 0; i < squareCardCollectionBean.getData().getItemList().size(); i++){
            parseFollowCard(viewModels, squareCardCollectionBean.getData().getItemList().get(i));
        }
    }

    private void parseFollowCard(List<BaseCustomViewModel> viewModels, FollowCardBean followCardBean) {
        FollowCardViewModel followCardViewModel = new FollowCardViewModel();
        followCardViewModel.coverUrl = followCardBean.getData().getContent().getData().getCover().getDetail();
        followCardViewModel.videoTime = followCardBean.getData().getContent().getData().getDuration();
        followCardViewModel.authorUrl = followCardBean.getData().getContent().getData().getAuthor().getIcon();
        followCardViewModel.description = followCardBean.getData().getContent().getData().getAuthor().getName()
                + "/ #"
                + followCardBean.getData().getContent().getData().getCategory();
        followCardViewModel.title =
                followCardBean.getData().getContent().getData().getTitle();
        followCardViewModel.nickName = followCardBean.getData().getContent().getData().getAuthor().getName();
        followCardViewModel.video_description = followCardBean.getData().getContent().getData().getDescription();
        followCardViewModel.userDescription = followCardBean.getData().getContent().getData().getAuthor().getDescription();
        followCardViewModel.playerUrl = followCardBean.getData().getContent().getData().getPlayUrl();
        followCardViewModel.blurredUrl = followCardBean.getData().getContent().getData().getCover().getBlurred();
        followCardViewModel.videoId = followCardBean.getData().getContent().getData().getId();
//        followCardViewModel.coverUrl.set(followCardBean.getData().getContent().getData().getCover().getDetail());
//        followCardViewModel.videoTime.set(followCardBean.getData().getContent().getData().getDuration());
//        followCardViewModel.authorUrl.set(followCardBean.getData().getContent().getData().getAuthor().getIcon());
//        followCardViewModel.description.set(followCardBean.getData().getContent().getData().getAuthor().getName()
//                + "/ #"
//                + followCardBean.getData().getContent().getData().getCategory());
//        followCardViewModel.title.set(followCardBean.getData().getContent().getData().getTitle());
//        followCardViewModel.nickName.set(followCardBean.getData().getContent().getData().getAuthor().getName());
//        followCardViewModel.setVideo_description(followCardBean.getData().getContent().getData().getDescription());
//        followCardViewModel.setUserDescription(followCardBean.getData().getContent().getData().getAuthor().getDescription());
//        followCardViewModel.setPlayerUrl(followCardBean.getData().getContent().getData().getPlayUrl());
//        followCardViewModel.setBlurredUrl(followCardBean.getData().getContent().getData().getCover().getBlurred());
//        followCardViewModel.setVideoId(followCardBean.getData().getContent().getData().getId());
        viewModels.add(followCardViewModel);
    }

    private void parseVideoCard(List<BaseCustomViewModel> viewModels, VideoSmallCardBean videoSmallCardBean) {
        VideoCardViewModel videoCardViewModel = new VideoCardViewModel();
        videoCardViewModel.coverUrl =
                videoSmallCardBean.getData().getCover().getDetail();
        videoCardViewModel.videoTime =
                videoSmallCardBean.getData().getDuration();
        videoCardViewModel.title = videoSmallCardBean.getData().getTitle();
        videoCardViewModel.description =
                videoSmallCardBean.getData().getAuthor().getName() + " / # "
                        + videoSmallCardBean.getData().getCategory();
        videoCardViewModel.authorUrl = videoSmallCardBean.getData().getAuthor().getIcon();
        videoCardViewModel.userDescription = videoSmallCardBean.getData().getAuthor().getDescription();
        videoCardViewModel.nickName = videoSmallCardBean.getData().getAuthor().getName();
        videoCardViewModel.video_description = videoSmallCardBean.getData().getDescription();
        videoCardViewModel.playerUrl = videoSmallCardBean.getData().getPlayUrl();
        videoCardViewModel.blurredUrl = videoSmallCardBean.getData().getCover().getBlurred();
        videoCardViewModel.videoId = videoSmallCardBean.getId();
        videoCardViewModel.collectionCount = videoSmallCardBean.getData().getConsumption().getCollectionCount();
        videoCardViewModel.shareCount = videoSmallCardBean.getData().getConsumption().getShareCount();
        viewModels.add(videoCardViewModel);
    }
}
