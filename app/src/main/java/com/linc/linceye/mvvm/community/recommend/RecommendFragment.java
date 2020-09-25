package com.linc.linceye.mvvm.community.recommend;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;

import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;

import com.linc.linceye.mvvm.community.recommend.adapter.RecommendAdapter;
import com.linc.linceye.utils.DensityUtils;
import com.linc.linceye.utils.RecyclerItemDecoration;

import java.util.List;

/**
 * <社区-推荐> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/24
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class RecommendFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, RecommendViewModel, BaseCustomViewModel>
        implements IRecommendView {

    public static RecommendFragment newInstance(){
        RecommendFragment fragment = new RecommendFragment();
        return fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new RecommendAdapter(getContext(), mData);
    }

    @Override
    protected void initView() {
        mBinding.recycleView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mBinding.recycleView.setLayoutManager(layoutManager);
        mBinding.recycleView.addItemDecoration(new RecyclerItemDecoration(DensityUtils.dp2px(getContext(), 3),
                0,
                DensityUtils.dp2px(getContext(), 3),
                DensityUtils.dp2px(getContext(), 5)));

        mViewModel.initModel();

        mBinding.recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int[] first = new int[2];
                layoutManager.findFirstVisibleItemPositions(first);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (first[0] == 1 || first[1] == 1)){
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });

    }

    @Override
    protected RecommendViewModel getViewModel() {
        return new ViewModelProvider(this).get(RecommendViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.load(isFirst);
    }

    @Override
    public void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst) {
        onHandleResponseData(viewModels, isFirst);
    }

    @Override
    protected void getFirstData() {
        mViewModel.load(true);
    }

    @Override
    public void onLoadMoreFailure(String message) {

    }

    @Override
    public void onLoadMoreEmpty() {

    }
}
