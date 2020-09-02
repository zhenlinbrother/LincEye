package com.linc.linceye.mvvm.community.attention;

import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.R;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.utils.DensityUtils;
import com.linc.linceye.utils.RecyclerItemDecoration;
import com.linceye.video.helper.ScrollCalculatorHelper;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

import java.util.List;
import java.util.Objects;

/**
 * <社区-关注> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AttentionFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, AttentionViewModel, BaseCustomViewModel>
        implements IAttentionView{

    private ScrollCalculatorHelper scrollCalculatorHelper;

    private boolean isFull = false;

    public static AttentionFragment newInstance(){
        AttentionFragment fragment = new AttentionFragment();
        return fragment;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new AttentionAdapter(getContext(), R.layout.community_item_attention_card_view, mData);
    }

    @Override
    protected void initView() {

        mBinding.recycleView.setHasFixedSize(true);
        int decoration = DensityUtils.dip2px(getContext(), 10);
        mBinding.recycleView.addItemDecoration(new RecyclerItemDecoration(decoration, 0, decoration, decoration));
        //限定范围为屏幕有一半 上下偏移180
        int playTop = CommonUtil.getScreenHeight(requireContext()) / 2
                - CommonUtil.dip2px(requireContext(), 180);
        int playBottom = CommonUtil.getScreenHeight(requireContext()) / 2
                + CommonUtil.dip2px(requireContext(), 180);
        //控制自动播放帮助类
        scrollCalculatorHelper = new ScrollCalculatorHelper(R.id.video_item_player, playTop, playBottom);

        //滑动监听
        mBinding.recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                scrollCalculatorHelper.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager == null){
                    return;
                }
                firstVisibleItem =
                        manager.findFirstVisibleItemPosition();
                lastVisibleItem =
                        manager.findLastVisibleItemPosition();

                //这是滑动自动播放的代码
                if (!isFull){
                    scrollCalculatorHelper.onScroll(recyclerView,
                            firstVisibleItem,
                            lastVisibleItem,
                            lastVisibleItem - firstVisibleItem);
                }
            }
        });

        mViewModel.initModel();
    }

    @Override
    protected AttentionViewModel getViewModel() {
        return new ViewModelProvider(this).get(AttentionViewModel.class);
    }

    @Override
    public void onDataLoadFinish(List<BaseCustomViewModel> viewModels, boolean isFirst) {
        onHandleResponseData(viewModels, isFirst);
    }

    @Override
    public void onLoadMoreFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setLoadError();
    }

    @Override
    protected void onFragmentResume() {
        super.onFragmentResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onFragmentPause() {
        super.onFragmentPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}
