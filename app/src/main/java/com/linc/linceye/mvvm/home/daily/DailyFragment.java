package com.linc.linceye.mvvm.home.daily;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.home.discover.adapter.DiscoverAdapter;

import java.util.List;
/**
 * <日常界面> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DailyFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, DailyViewModel, BaseCustomViewModel>
        implements IDailyView {

    public static DailyFragment newInstance(){
        DailyFragment fragment = new DailyFragment();
        return fragment;
    }
    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new DailyAdapter(getContext(), mData);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected DailyViewModel getViewModel() {
        return new ViewModelProvider(this).get(DailyViewModel.class);
    }

    @Override
    public void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst) {
        onHandleResponseData(viewModels, isFirst);
    }

    @Override
    public void onLoadMoreFailure(String message) {
        mBaseAdapter.setLoadError();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setLoadError();
    }
}
