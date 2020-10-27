package com.linc.linceye.mvvm.download;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.linc.base.mvvm.activity.SimpleLoadMoreActivity;
import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.download.bean.DownloadInfo;
import com.linc.linceye.R;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.databinding.ActivityDownloadBinding;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;

import java.util.List;

/**
 * <视频下载页面> <功能详细描述>
 *
 * @author linc
 * @version 2020/10/26
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DownloadActivity
        extends SimpleLoadMoreActivity<ActivityDownloadBinding, DownloadViewModel, DownloadInfo>
        implements IDownloadView{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.initModel();
        ImmersionBar.with(this)
                .statusBarColor(R.color.common_white)
                .navigationBarColor(R.color.common_white)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new DownloadAdapter(this, mData);
    }


    @Override
    protected DownloadViewModel getViewModel() {
        return new ViewModelProvider(this).get(DownloadViewModel.class);
    }

    @Override
    protected void initIntent() {

    }

    @Override
    public void onLoadMoreFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setNoMore();
    }

    @Override
    public void onDataLoadFinish(List<DownloadInfo> viewModels, boolean isFirst) {
        mBaseAdapter.onSuccess();
        onHandleResponse(viewModels, isFirst);
    }

    public void onHandleResponse(List<DownloadInfo> dataList, boolean isFirst){

        if (isFirst){
            mData.clear();

            if ((dataList == null || dataList.size() <= 0)){
                mBaseAdapter.onEmpty();
                return;
            }

            mBaseAdapter.onSuccess();
        }

        if (dataList == null || dataList.size() < 10){
            mBaseAdapter.setNoMore();
        } else {
            mBaseAdapter.setLoadComplete();
        }

        if (dataList != null){
            mData.addAll(dataList);
        }

        mBaseAdapter.notifyDataSetChanged();
    }
}
