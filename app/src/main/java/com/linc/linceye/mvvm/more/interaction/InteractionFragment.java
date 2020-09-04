package com.linc.linceye.mvvm.more.interaction;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.R;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

/**
 * <通知-互动> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class InteractionFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, InteractionViewModel, ThemesItemViewModel>
        implements IInteractionView{

    public static InteractionFragment newInstance(){
        InteractionFragment fragment = new InteractionFragment();
        return fragment;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new InteractionAdapter(getContext(), R.layout.more_item_themes_view, mData);
    }

    @Override
    protected void initView() {
        mViewModel.initModel();
    }

    @Override
    protected InteractionViewModel getViewModel() {
        return new ViewModelProvider(this).get(InteractionViewModel.class);
    }

    @Override
    public void onDataLoaded(List<ThemesItemViewModel> data, boolean isFirst) {
        onHandleResponseData(data, isFirst);
    }

    @Override
    public void onLoadMoreFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mBaseAdapter.setNoMore();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setNoMore();
    }
}
