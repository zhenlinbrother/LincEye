package com.linc.linceye.mvvm.more.themes.childpager;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.MoreItemThemesViewBinding;
import com.linc.linceye.mvvm.more.themes.childpager.bean.ThemesItemViewModel;

import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/3
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class ThemeContentAdapter extends SimpleDataBindingAdapter<ThemesItemViewModel, MoreItemThemesViewBinding> {
    protected ThemeContentAdapter(Context context, int layout, List<ThemesItemViewModel> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(MoreItemThemesViewBinding binding, ThemesItemViewModel item, RecyclerView.ViewHolder holder) {
        binding.setViewModel(item);
        binding.executePendingBindings();
    }
}
