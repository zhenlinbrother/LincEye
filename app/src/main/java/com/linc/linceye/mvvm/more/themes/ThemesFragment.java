package com.linc.linceye.mvvm.more.themes;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.tabs.TabLayout;
import com.linc.base.mvvm.fragment.BaseLazyMvvmFragment;
import com.linc.linceye.R;
import com.linc.linceye.databinding.MoreFragmentThemesBinding;
import com.linc.linceye.mvvm.more.themes.adapter.ThemesFragmentPageAdapter;
import com.linc.linceye.mvvm.more.themes.bean.Tabs;

import java.util.List;

/**
 * <通知-主题> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemesFragment
        extends BaseLazyMvvmFragment<MoreFragmentThemesBinding, ThemeFragmentViewModel>
        implements IThemeView {

    private ThemesFragmentPageAdapter pageAdapter;

    public static ThemesFragment newInstance(){
        ThemesFragment fragment = new ThemesFragment();
        return fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
    }

    private void initView() {
        mBinding.vpContent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));
        mBinding.vpContent.setOffscreenPageLimit(1);
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.vpContent.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pageAdapter = new ThemesFragmentPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        mBinding.vpContent.setAdapter(pageAdapter);
        mViewModel.initModel();
    }

    @Override
    protected void getFirstData() { }

    @Override
    protected int getLayoutId() {
        return R.layout.more_fragment_themes;
    }

    @Override
    protected ThemeFragmentViewModel getViewModel() {
        return new ViewModelProvider(this).get(ThemeFragmentViewModel.class);
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }

    @Override
    public void onDataLoaded(List<Tabs> tabs) {
        pageAdapter.setData(tabs);
        mBinding.tabLayout.removeAllTabs();
        for (Tabs tab : tabs){
            mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText(tab.getName()));
        }
        mBinding.tabLayout.scrollTo(0, 0);
    }
}
