package com.linc.linceye.mvvm.home.discover;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.home.discover.adapter.DiscoverAdapter;

import java.util.List;
/**
 * <发现页面> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/18
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DiscoverFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, DiscoverViewModel, BaseCustomViewModel>
        implements IDiscoverView {

    public static DiscoverFragment newInstance(){
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.initModel();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new DiscoverAdapter(getContext(), mData);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected DiscoverViewModel getViewModel() {
        return new ViewModelProvider(this).get(DiscoverViewModel.class);
    }

    @Override
    public void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isEmpty) {
        mBaseAdapter.onSuccess();
        onHandleResponseData(viewModels, isEmpty);

    }

    @Override
    public boolean requestLoadMore() {
        return false;
    }
}
