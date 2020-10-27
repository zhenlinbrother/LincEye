package com.linc.linceye.mvvm.download;

import com.linc.download.bean.CurStatus;
import com.linc.download.bean.DownloadInfo;
import com.linc.download.jerry.JerryDownload;
import com.linc.linceye.base.model.BasePagingModel;
import com.linc.linceye.net.rx.RxObservableSubscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/26
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadModel<T> extends BasePagingModel<T> {

    private int start = 1;
    private int size = 10;

    public void loadData(boolean isFirst) {

        Observable.just(true)
                .map(new Function<Boolean, List<DownloadInfo>>() {
                    @Override
                    public List<DownloadInfo> apply(Boolean aBoolean) throws Throwable {

                        List<DownloadInfo> downloadInfoList
                                = JerryDownload.getInstance().getDownloadInfos(start, size);

                        List<DownloadInfo> finishList = new ArrayList<>();

                        for (DownloadInfo downloadInfo : downloadInfoList) {
                            if (downloadInfo.isCurStatusContains(CurStatus.FINISH)){
                                finishList.add(downloadInfo);
                            }
                        }

                        return finishList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscribe<List<DownloadInfo>>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        loadFail(code + message, isFirst);
                    }

                    @Override
                    protected void onSuccessRes(List<DownloadInfo> value) {
                        loadSuccess((T) value, value.size() <= 0, isFirst);
                        size += 10;
                    }
                });
    }
}
