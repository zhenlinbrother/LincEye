package com.linc.linceye.mvvm.more.themes.childpager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.base.mvvm.fragment.BaseLazyListMvvmFragment;
import com.linc.base.mvvm.fragment.SimpleLoadMoreLazyFragment;
import com.linc.linceye.R;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.BaseRecyclerViewBinding;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

/**
 * <通知-关注-子页面> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemeContentFragment
        extends SimpleLoadMoreLazyFragment<BaseRecyclerViewBinding, ThemeContentViewModel, ThemesItemViewModel>
        implements IThemeContentView{

    private String url;
    private int position;

    public static ThemeContentFragment newInstance(int position, String api){
        Bundle args = new Bundle();

        args.putString("URL", api);
        args.putInt("position", position);

        ThemeContentFragment fragment = new ThemeContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle arguments) {
        url = arguments.getString("URL");
        position = arguments.getInt("position");
    }

    @Override
    protected void loadData(boolean isFirst) {
        mViewModel.loadData(position, url, isFirst);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ThemeContentAdapter(getContext(), R.layout.more_item_themes_view, mData);
    }

    @Override
    protected void initView() {
        mViewModel.initModel();
    }

    @Override
    protected ThemeContentViewModel getViewModel() {
        return new ViewModelProvider(this).get(ThemeContentViewModel.class);
    }

    @Override
    public void onDataLoaded(List<ThemesItemViewModel> viewModels, boolean isFirstPage) {
        onHandleResponseData(viewModels, isFirstPage);
    }

    @Override
    public void onLoadMoreFailure(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mBaseAdapter.setLoadError();
    }

    @Override
    public void onLoadMoreEmpty() {
        mBaseAdapter.setNoMore();
    }
}
