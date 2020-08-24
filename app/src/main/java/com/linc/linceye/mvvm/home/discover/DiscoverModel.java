package com.linc.linceye.mvvm.home.discover;

import androidx.annotation.NonNull;

import com.linc.linceye.base.model.BaseModel;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.mvvm.home.discover.bean.BannerBean;
import com.linc.linceye.mvvm.home.discover.bean.BriefCard;
import com.linc.linceye.mvvm.home.discover.bean.CategoryCardBean;
import com.linc.linceye.mvvm.home.discover.bean.SubjectCardBean;
import com.linc.linceye.mvvm.home.discover.bean.TextCardbean;
import com.linc.linceye.mvvm.home.discover.bean.TopBannerBean;
import com.linc.linceye.mvvm.home.discover.bean.viewmodel.BriefCardViewModel;
import com.linc.linceye.mvvm.home.discover.bean.viewmodel.ContentBannerViewModel;
import com.linc.linceye.mvvm.home.discover.bean.viewmodel.TopBannerViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.VideoSmallCardBean;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.TitleViewModel;
import com.linc.linceye.mvvm.home.nominate.bean.viewmodel.VideoCardViewModel;
import com.linc.linceye.net.RetrofitFactory;
import com.linc.linceye.net.rx.RxParser;
import com.linc.linceye.net.rx.RxSingleSubscriber;
import com.linc.linceye.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Function;

public class DiscoverModel<T> extends BaseModel<T> {
    
    public void loadData(){
        RetrofitFactory
                .getApiService()
                .getDiscoverData()
                .compose(RxParser.handleSingleToResult1())
                .map(new Function<JSONObject, List<BaseCustomViewModel>>() {

                    @Override
                    public List<BaseCustomViewModel> apply(JSONObject jsonObject) throws Throwable {
                        List<BaseCustomViewModel> viewModels = new ArrayList<>();
                        JSONArray itemList = jsonObject.optJSONArray("itemList");
                        if (itemList != null){

                            for (int i = 0; i < itemList.length(); i++){

                                JSONObject currentObject = itemList.getJSONObject(i);
                                switch (currentObject.optString("type")){
                                    case "horizontalScrollCard":
                                        TopBannerBean topBannerBean = GsonUtils.fromLocalJson(currentObject.toString(), TopBannerBean.class);
                                        TopBannerViewModel topBannerViewModel = new TopBannerViewModel();
                                        topBannerViewModel.bannerUrl = topBannerBean.getData().getItemList().get(0).getData().getImage();
                                        viewModels.add(topBannerViewModel);
                                        break;
                                    case "specialSquareCardCollection":
                                        CategoryCardBean categoryCardBean = GsonUtils.fromLocalJson(currentObject.toString(),CategoryCardBean.class);
                                        viewModels.add(categoryCardBean);
                                        break;
                                    case "columnCardList":
                                        SubjectCardBean subjectCardBean = GsonUtils.fromLocalJson(currentObject.toString(),SubjectCardBean.class);
                                        viewModels.add(subjectCardBean);
                                        break;
                                    case "textCard":
                                        TextCardbean textCardbean = GsonUtils.fromLocalJson(currentObject.toString(),TextCardbean.class);
                                        TitleViewModel titleViewModel = new TitleViewModel();
                                        titleViewModel.title = textCardbean.getData().getText();
                                        titleViewModel.actionTitle = textCardbean.getData().getRightText();
                                        viewModels.add(titleViewModel);
                                        break;
                                    case "banner":
                                        BannerBean bannerBean = GsonUtils.fromLocalJson(currentObject.toString(),BannerBean.class);
                                        ContentBannerViewModel bannerViewModel = new ContentBannerViewModel();
                                        bannerViewModel.bannerUrl = bannerBean.getData().getImage();
                                        viewModels.add(bannerViewModel);
                                        break;
                                    case "videoSmallCard":
                                        VideoSmallCardBean videoSmallCardBean = GsonUtils
                                                .fromLocalJson(currentObject.toString(),
                                                        VideoSmallCardBean.class);
                                        paresVideoCard(viewModels, videoSmallCardBean);
                                        break;
                                    case "briefCard":
                                        BriefCard briefCard = GsonUtils.fromLocalJson(currentObject.toString(),BriefCard.class);
                                        BriefCardViewModel briefCardViewModel = new BriefCardViewModel();
                                        briefCardViewModel.coverUrl = briefCard.getData().getIcon();
                                        briefCardViewModel.title = briefCard.getData().getTitle();
                                        briefCardViewModel.description = briefCard.getData().getDescription();
                                        viewModels.add(briefCardViewModel);
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
                        loadFail(message, true);
                    }

                    @Override
                    protected void onSuccessRes(@NonNull List<BaseCustomViewModel> value) {
                        loadSuccess((T) value, value.size() <= 0, true);
                    }
                });
    }

    private void paresVideoCard(List<BaseCustomViewModel> viewModels,
                                VideoSmallCardBean videoSmallCardBean)
    {
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
        videoCardViewModel.videoId = videoSmallCardBean.getData().getId();
        videoCardViewModel.collectionCount = videoSmallCardBean.getData().getConsumption().getCollectionCount();
        videoCardViewModel.shareCount = videoSmallCardBean.getData().getConsumption().getShareCount();
        viewModels.add(videoCardViewModel);
    }
}
