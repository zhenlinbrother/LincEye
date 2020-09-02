package com.linc.linceye.mvvm.community;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.linc.base.mvvm.fragment.BaseLazyMvvmFragment;
import com.linc.base.mvvm.viewmodel.IMvvmBaseViewModel;
import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonPageAdapter;
import com.linc.linceye.databinding.FragmentCommunityBinding;
import com.linc.linceye.mvvm.community.attention.AttentionFragment;
import com.linc.linceye.mvvm.community.recommend.RecommendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <社区> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/24
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommunityFragment extends BaseLazyMvvmFragment<FragmentCommunityBinding, IMvvmBaseViewModel> {

    List<Fragment> fragments = new ArrayList<>();
    private CommonPageAdapter pageAdapter;
    private String[] tables = {"推荐","关注"};

    @Override
    protected void getFirstData() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        fragments.add(RecommendFragment.newInstance());
        fragments.add(AttentionFragment.newInstance());
        initView();
    }

    private void initView() {
        pageAdapter = new CommonPageAdapter(getChildFragmentManager(), fragments, tables);
        mBinding.vpHomeContent.setOffscreenPageLimit(1);
        mBinding.vpHomeContent.setAdapter(pageAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.vpHomeContent);
        mBinding.vpHomeContent.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabLayout));
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
