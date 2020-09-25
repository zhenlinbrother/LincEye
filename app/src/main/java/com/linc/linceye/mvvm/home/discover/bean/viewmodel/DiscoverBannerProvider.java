package com.linc.linceye.mvvm.home.discover.bean.viewmodel;

import android.view.View;
import android.widget.ImageView;

import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonBindingAdapters;
import com.linc.linceye.mvvm.home.discover.bean.TopBannerBean;
import com.zhpan.bannerview.holder.ViewHolder;

public class DiscoverBannerProvider implements ViewHolder<TopBannerBean.DataBeanX.ItemListBean> {
    @Override
    public int getLayoutId() {
        return R.layout.home_item_banner_item_view;
    }

    @Override
    public void onBind(View itemView, TopBannerBean.DataBeanX.ItemListBean data, int position, int size) {
        ImageView imageView = itemView.findViewById(R.id.banner_bg);
        CommonBindingAdapters.loadImage(imageView, data.getData().getImage());
    }
}
