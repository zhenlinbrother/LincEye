package com.linc.linceye.mvvm.player;

import android.util.Log;


import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.player.bean.LeftAlignTextHeader;
import com.linc.linceye.mvvm.player.bean.ReplyBean;
import com.linc.linceye.mvvm.player.bean.TextCard;
import com.linc.linceye.mvvm.player.bean.VideoSmallCard;
import com.linc.linceye.mvvm.player.bean.viewmodel.ReplyViewModel;
import com.linc.linceye.mvvm.player.bean.viewmodel.VideoCardViewModel;
import com.linc.linceye.mvvm.player.bean.viewmodel.VideoTextViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxObservableSubscribe;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class VideoPlayerModel<T> extends BasePagingModel<T> {

    private static final String TAG = "fwerwer";

    private int mVideoId;
    private ArrayList<BaseCustomViewModel> viewModels = new ArrayList<>();
    public void loadData(boolean isFirst, int videoId){
        Observable<JSONObject> nominateObservable = RetrofitFactory
                .getApiService()
                .getRecommendData(videoId)
                .compose(RxParser.handleObservableToResult());
        Observable<JSONObject> replyObservable = RetrofitFactory.getApiService().getRetryData(videoId)
                .compose(RxParser.handleObservableToResult());

        Observable.zip(nominateObservable, replyObservable, new BiFunction<JSONObject, JSONObject, List<BaseCustomViewModel>>() {
            @Override
            public List<BaseCustomViewModel> apply(JSONObject jsonObject, JSONObject jsonObject2) throws Throwable {
                parseNominateData(viewModels, jsonObject);
                parseReplyData(viewModels, jsonObject2);
                return viewModels;
            }
        }).subscribe(new RxObservableSubscribe<List<BaseCustomViewModel>>(mySelf) {
            @Override
            protected void onError(int code, String message) {
                Log.d(TAG, "onError: ");
                loadFail(message, true);
            }

            @Override
            protected void onSuccessRes(List<BaseCustomViewModel> value) {
                Log.d(TAG, "onSuccessRes: ");
                loadSuccess((T)value, value.size() <= 0, true);
            }
        });
    }

    private void parseNominateData(ArrayList<BaseCustomViewModel> viewModels, JSONObject jsonObject) {
        try {
            JSONArray itemList = jsonObject.optJSONArray("itemList");
            if (itemList != null){

                for (int i = 0; i < itemList.length(); i++){
                    JSONObject currentObject = itemList.getJSONObject(i);
                    switch (currentObject.optString("type")){
                        case "textCard":
                            TextCard textCard = GsonUtils.fromLocalJson(
                                    currentObject.toString(),
                                    TextCard.class);
                            VideoTextViewModel textViewModel =
                                    new VideoTextViewModel();
                            textViewModel.textTitle =
                                    textCard.getData().getText();
                            viewModels.add(textViewModel);
                            break;
                        case "videoSmallCard":
                            VideoSmallCard videoSmallCard = GsonUtils
                                    .fromLocalJson(currentObject.toString(),
                                            VideoSmallCard.class);
                            paresVideoCard(viewModels, videoSmallCard);
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseReplyData(ArrayList<BaseCustomViewModel> viewModels,
                                JSONObject jsonObject)
    {
        try
        {
            JSONArray itemList = jsonObject.optJSONArray("itemList");
            if (itemList != null)
            {
                for (int i = 0; i < itemList.length(); i++)
                {
                    JSONObject ccurrentObject = itemList.getJSONObject(i);
                    switch (ccurrentObject.optString("type"))
                    {
                        case "leftAlignTextHeader":
                            LeftAlignTextHeader alignTextHeader = GsonUtils
                                    .fromLocalJson(ccurrentObject.toString(),
                                            LeftAlignTextHeader.class);
                            VideoTextViewModel textViewModel =
                                    new VideoTextViewModel();
                            textViewModel.textTitle =
                                    alignTextHeader.getData().getText();
                            viewModels.add(textViewModel);
                            break;
                        case "reply":
                            ReplyBean reply = GsonUtils.fromLocalJson(
                                    ccurrentObject.toString(),
                                    ReplyBean.class);
                            ReplyViewModel replyViewModel =
                                    new ReplyViewModel();
                            if (reply != null)
                            {
                                replyViewModel.avatar =
                                        reply.getData().getUser().getAvatar();
                                replyViewModel.nickName =
                                        reply.getData().getUser().getNickname();
                                replyViewModel.replyMessage =
                                        reply.getData().getMessage();
                                replyViewModel.releaseTime =
                                        reply.getData().getUser().getReleaseDate();
                                replyViewModel.likeCount =
                                        reply.getData().getLikeCount();
                                viewModels.add(replyViewModel);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void paresVideoCard(List<BaseCustomViewModel> viewModels,
                                VideoSmallCard videoSmallCard)
    {
        if (videoSmallCard == null)
        {
            return;
        }
        VideoCardViewModel videoCardViewModel = new VideoCardViewModel();
        videoCardViewModel.coverUrl =
                videoSmallCard.getData().getCover().getDetail();
        videoCardViewModel.videoTime = videoSmallCard.getData().getDuration();
        videoCardViewModel.title = videoSmallCard.getData().getTitle();
        videoCardViewModel.description =
                videoSmallCard.getData().getAuthor().getName() + " / # "
                        + videoSmallCard.getData().getCategory();
        videoCardViewModel.authorUrl =
                videoSmallCard.getData().getAuthor().getIcon();
        videoCardViewModel.userDescription =
                videoSmallCard.getData().getAuthor().getDescription();
        videoCardViewModel.nickName =
                videoSmallCard.getData().getAuthor().getName();
        videoCardViewModel.video_description =
                videoSmallCard.getData().getDescription();
        videoCardViewModel.playerUrl = videoSmallCard.getData().getPlayUrl();
        videoCardViewModel.blurredUrl =
                videoSmallCard.getData().getCover().getBlurred();
        videoCardViewModel.videoId = videoSmallCard.getData().getId();
        videoCardViewModel.collectionCount =
                videoSmallCard.getData().getConsumption().getCollectionCount();
        videoCardViewModel.shareCount =
                videoSmallCard.getData().getConsumption().getShareCount();
        viewModels.add(videoCardViewModel);
    }

}
