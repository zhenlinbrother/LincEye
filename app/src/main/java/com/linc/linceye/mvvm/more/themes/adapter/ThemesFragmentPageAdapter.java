package com.linc.linceye.mvvm.more.themes.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.linc.linceye.mvvm.more.themes.bean.Tabs;
import com.linc.linceye.mvvm.more.themes.childpager.ThemeContentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/2
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemesFragmentPageAdapter extends FragmentStatePagerAdapter {

    private List<Tabs> tabs;

    public ThemesFragmentPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setData(List<Tabs> data){
        if (data == null){
            return;
        }

        if (tabs == null){
            tabs = new ArrayList<>();
        }

        this.tabs = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ThemeContentFragment.newInstance(position, tabs.get(position).getApiUrl());
    }

    @Override
    public int getCount() {
        return tabs != null && tabs.size() > 0 ? tabs.size() : 0;
    }
}
