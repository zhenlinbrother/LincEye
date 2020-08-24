package com.linc.linceye.mvvm.home.discover.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linc.linceye.R;
import com.linc.linceye.base.adapter.CommonSimpleAdapter;
import com.linc.linceye.base.viewholder.BaseViewHolder;
import com.linc.linceye.base.viewmodel.BaseCustomViewModel;
import com.linc.linceye.databinding.HomeItemCategoryCardViewBinding;
import com.linc.linceye.databinding.HomeItemSubjectCardViewBinding;
import com.linc.linceye.mvvm.home.discover.bean.CategoryCardBean;
import com.linc.linceye.mvvm.home.discover.bean.SquareCard;
import com.linc.linceye.mvvm.home.discover.bean.SubjectCardBean;
import com.linc.linceye.mvvm.home.discover.bean.viewmodel.IDisCoverItemType;
import com.linc.linceye.mvvm.home.discover.bean.viewmodel.TopBannerViewModel;
import com.linc.linceye.mvvm.home.nominate.adapter.NominateItemType;
import com.linc.linceye.utils.DensityUtils;
import com.linc.linceye.utils.RecyclerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends CommonSimpleAdapter<BaseCustomViewModel> {
    public DiscoverAdapter(Context context, List<BaseCustomViewModel> viewModels) {
        super(context, viewModels);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        RecyclerView.ViewHolder holder = null;
        switch (viewType){
            case IDisCoverItemType.TOP_BANNER_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_top_banner_view, parent, false);
                holder = new TopBannerViewHolder(binding);
                break;
            case IDisCoverItemType.CATEGORY_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_category_card_view, parent, false);
                holder = new CategoryCardViewHolder(binding);
                break;
            case IDisCoverItemType.SUBJECT_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_subject_card_view, parent, false);
                holder = new SubjectCardViewHolder(binding);
                break;
            case IDisCoverItemType.CONTENT_BANNER_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_content_banner_view, parent, false);
                holder = new ContentBannerViewHolder(binding);
                break;
            case NominateItemType.TITLE_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_title_left_right_view, parent, false);
                holder = new TitleViewHolder(binding);
                break;
            case NominateItemType.VIDEO_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_video_card_view, parent, false);
                holder = new VideoCardViewHolder(binding);
                break;
            case IDisCoverItemType.THEME_CARD_VIEW:
                binding = DataBindingUtil.inflate(mInflater, R.layout.home_item_brief_card_view, parent, false);
                holder = new ThemeViewHolder(binding);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TopBannerViewHolder){
            TopBannerViewHolder topBannerViewModel = (TopBannerViewHolder) holder;
            topBannerViewModel.bindData(mData.get(position));
        } else if (holder instanceof CategoryCardViewHolder){
            CategoryCardViewHolder categoryCardViewHolder = (CategoryCardViewHolder) holder;

            HomeItemCategoryCardViewBinding binding =
                    (HomeItemCategoryCardViewBinding) categoryCardViewHolder.getBinding();
            initCategoryCardView(binding, mData.get(position));
        } else if (holder instanceof SubjectCardViewHolder){
            SubjectCardViewHolder subjectCardViewHolder = (SubjectCardViewHolder) holder;

            HomeItemSubjectCardViewBinding binding =
                    (HomeItemSubjectCardViewBinding) subjectCardViewHolder.getBinding();
            initSubjectCardView(binding, mData.get(position));
        } else if (holder instanceof ContentBannerViewHolder){
            ContentBannerViewHolder contentBannerViewHolder = (ContentBannerViewHolder) holder;
            contentBannerViewHolder.bindData(mData.get(position));
        } else if (holder instanceof TitleViewHolder){
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.bindData(mData.get(position));
        } else if (holder instanceof VideoCardViewHolder){
            VideoCardViewHolder videoCardViewHolder = (VideoCardViewHolder) holder;
            videoCardViewHolder.bindData(mData.get(position));
        } else {
            ThemeViewHolder themeViewHolder = (ThemeViewHolder) holder;
            themeViewHolder.bindData(mData.get(position));
        }
    }

    private void initCategoryCardView(HomeItemCategoryCardViewBinding binding, BaseCustomViewModel baseCustomViewModel) {
        binding.rvCategoryView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext.get(), 2);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvCategoryView.setLayoutManager(gridLayoutManager);
        binding.rvCategoryView.addItemDecoration(new RecyclerItemDecoration(0,0,
                DensityUtils.dip2px(mContext.get(), 5), 0));

        CategoryCardBean cardBean = (CategoryCardBean) baseCustomViewModel;
        binding.tvTitle.setText(cardBean.getData().getHeader().getTitle());
        binding.tvActionTitle.setText(cardBean.getData().getHeader().getRightText());
        ArrayList<SquareCard> dataList = new ArrayList<>();
        for (CategoryCardBean.DataBeanX.ItemListBean itemListBean : cardBean.getData().getItemList()) {
            dataList.add(itemListBean.getData());
        }
        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(mContext.get(),
                R.layout.home_item_category_item_card_view, dataList);
        binding.rvCategoryView.setAdapter(categoryItemAdapter);
    }

    private void initSubjectCardView(HomeItemSubjectCardViewBinding binding, BaseCustomViewModel baseCustomViewModel) {
        binding.rvCategoryView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext.get(), 2);
        binding.rvCategoryView.setLayoutManager(gridLayoutManager);
        binding.rvCategoryView.addItemDecoration(new RecyclerItemDecoration(0,
                0, DensityUtils.dip2px(mContext.get(), 5), DensityUtils.dip2px(mContext.get(), 5)));

        SubjectCardBean cardBean = (SubjectCardBean) baseCustomViewModel;
        binding.tvTitle.setText(cardBean.getData().getHeader().getTitle());
        binding.tvActionTitle.setText(cardBean.getData().getHeader().getRightText());

        ArrayList<SquareCard> dataList = new ArrayList<>();
        for (SubjectCardBean.DataBeanX.ItemListBean itemListBean : cardBean.getData().getItemList()) {
            dataList.add(itemListBean.getData());
        }

        SubjectItemAdapter subjectItemAdapter = new SubjectItemAdapter(mContext.get(),
                R.layout.home_item_category_item_subject_card_view, dataList);
        binding.rvCategoryView.setAdapter(subjectItemAdapter);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).type();
    }

    public static class TopBannerViewHolder extends BaseViewHolder{

        public TopBannerViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class CategoryCardViewHolder extends BaseViewHolder{

        public CategoryCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class SubjectCardViewHolder extends BaseViewHolder{

        public SubjectCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class ContentBannerViewHolder extends BaseViewHolder{
        public ContentBannerViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class TitleViewHolder extends BaseViewHolder{
        public TitleViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class VideoCardViewHolder extends BaseViewHolder{
        public VideoCardViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }

    public static class ThemeViewHolder extends BaseViewHolder{
        public ThemeViewHolder(ViewDataBinding binding) {
            super(binding);
        }
    }
}
