package com.linc.linceye.mvvm.more;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.linc.base.mvvm.fragment.BaseLazyMvvmFragment;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonPageAdapter;
import com.linc.linceye.databinding.FragmentCommunityBinding;
import com.linc.linceye.mvvm.more.interaction.InteractionFragment;
import com.linc.linceye.mvvm.more.push.PushFragment;
import com.linc.linceye.mvvm.more.themes.ThemesFragment;

import java.util.ArrayList;
import java.util.List;

public class MoreFragment extends BaseLazyMvvmFragment<FragmentCommunityBinding, IMvvmBaseViewModel> {

    private List<Fragment> fragments = new ArrayList<>();
    private CommonPageAdapter pageAdapter;
    private String[] tables = {"主题", "推送", "互动"};
    @Override
    protected void getFirstData() { }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        fragments.add(ThemesFragment.newInstance());
        fragments.add(PushFragment.newInstance());
        fragments.add(InteractionFragment.newInstance());
        initView();
    }

    private void initView() {
        pageAdapter = new CommonPageAdapter(getChildFragmentManager(), fragments, tables);
        mBinding.vpHomeContent.setOffscreenPageLimit(1);
        mBinding.vpHomeContent.setAdapter(pageAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.vpHomeContent);
        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.vpHomeContent.setCurrentItem(tab.getPosition());
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
    protected int getLayoutId() {
        return R.layout.fragment_community;
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
