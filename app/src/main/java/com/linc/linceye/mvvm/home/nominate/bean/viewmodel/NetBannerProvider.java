package com.linc.linceye.mvvm.home.nominate.bean.viewmodel;

import android.view.View;
import android.widget.ImageView;

import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonBindingAdapters;
import com.zhpan.bannerview.holder.ViewHolder;

public class NetBannerProvider implements ViewHolder<String> {
    @Override
    public int getLayoutId() {
        return R.layout.home_item_banner_item_view;
    }

    @Override
    public void onBind(View itemView, String data, int position, int size) {
        ImageView imageView = itemView.findViewById(R.id.banner_bg);
        CommonBindingAdapters.loadImage(imageView, data);
    }
}
