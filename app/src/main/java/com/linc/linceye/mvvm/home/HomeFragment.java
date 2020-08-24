package com.linc.linceye.mvvm.home;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.linc.base.mvvm.fragment.BaseLazyMvvmFragment;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.base.web.BaseWebViewFragment;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.BasePageAdapter;
import com.linc.linceye.base.adapter.CommonPageAdapter;

import com.linc.linceye.base.listener.BindingAdapterItem;
import com.linc.linceye.databinding.FragmentHomeBinding;
import com.linc.linceye.mvvm.home.daily.DailyFragment;
import com.linc.linceye.mvvm.home.discover.DiscoverFragment;
import com.linc.linceye.mvvm.home.nominate.INominateView;
import com.linc.linceye.mvvm.home.nominate.NominateFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseLazyMvvmFragment<FragmentHomeBinding, IMvvmBaseViewModel> {

    List<Fragment> fragments = new ArrayList<>();
    private CommonPageAdapter pageAdapter;
    private String[] tabTitles = {"发现", "推荐", "日常","淘宝"};

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        fragments.add(DiscoverFragment.newInstance());
        fragments.add(NominateFragment.newInstance());
        fragments.add(DailyFragment.newInstance());
        fragments.add(BaseWebViewFragment.newInstance("https://main.m.taobao.com/index.html"));
        initView();
        mBinding.viewPage.setCurrentItem(1);
    }

    private void initView() {
        pageAdapter = new CommonPageAdapter(getChildFragmentManager(), fragments, tabTitles);
        mBinding.viewPage.setOffscreenPageLimit(1);
        mBinding.viewPage.setAdapter(pageAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPage);
        mBinding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPage.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void getFirstData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected IMvvmBaseViewModel getViewModel() {
        return null;
    }

    @Override
    protected int getBindingVariable() {
        return 0;
    }


}
