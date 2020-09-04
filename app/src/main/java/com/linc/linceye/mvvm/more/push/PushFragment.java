package com.linc.linceye.mvvm.more.push;

import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.R;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.more.push.bean.MessageViewModel;

import java.util.List;
/**
 * <通知-社区> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PushFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, PushViewModel, MessageViewModel>
        implements IPushView {

    public static PushFragment newInstance(){
        PushFragment fragment = new PushFragment();
        return fragment;
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new PushAdapter(getContext(), R.layout.more_item_push_view, mData);
    }

    @Override
    protected void initView() {
        mViewModel.initModel();
    }

    @Override
    protected PushViewModel getViewModel() {
        return new ViewModelProvider(this).get(PushViewModel.class);
    }

    @Override
    public void onDataLoaded(List<MessageViewModel> data, boolean isFirstPage) {
        onHandleResponseData(data, isFirstPage);
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
