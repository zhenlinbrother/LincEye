package com.linc.base.mvvm.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * <通用页面适配器> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/19
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class CommonPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String[] tabTitles;

    public CommonPageAdapter(@NonNull FragmentManager fm, List<Fragment> fragments, String[] titles) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_SET_USER_VISIBLE_HINT);
        mFragments = fragments;
        tabTitles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
