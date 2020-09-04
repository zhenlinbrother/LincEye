package com.linc.linceye.mvvm.more.push;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.base.adapter.SimpleDataBindingAdapter;
import com.linc.linceye.databinding.MoreItemPushViewBinding;
import com.linc.linceye.mvvm.more.push.bean.MessageViewModel;

import java.util.List;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/9/4
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class PushAdapter extends SimpleDataBindingAdapter<MessageViewModel, MoreItemPushViewBinding> {
    protected PushAdapter(Context context, int layout, List<MessageViewModel> data) {
        super(context, layout, data);
    }

    @Override
    protected void onBindItem(MoreItemPushViewBinding binding, MessageViewModel item, RecyclerView.ViewHolder holder) {
        binding.setViewModel(item);
        binding.executePendingBindings();
    }
}
