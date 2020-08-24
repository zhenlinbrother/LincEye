package com.linc.lrecyclerview.swipe;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.linc.lrecyclerview.R;


/**
 * <包含侧滑的 ViewHolder> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class LSwipeViewHolder extends RecyclerView.ViewHolder {
    private static final int NONE = -1;
    private FrameLayout flLeftMenu;
    private FrameLayout flContent;
    private FrameLayout flRightMenu;
    private LSwipeItemLayout swipeItemLayout;

    public LSwipeViewHolder(View itemView) {
        super(itemView);
        this.swipeItemLayout = (LSwipeItemLayout)itemView.findViewById(R.id.swipe_item_layout);
        this.flLeftMenu = (FrameLayout)itemView.findViewById(R.id.fl_left_menu);
        this.flContent = (FrameLayout)itemView.findViewById(R.id.fl_content);
        this.flRightMenu = (FrameLayout)itemView.findViewById(R.id.fl_right_menu);
        this.flLeftMenu.removeAllViews();
        this.flRightMenu.removeAllViews();
        this.flContent.removeAllViews();
        if (this.getLeftMenuLayout() != -1) {
            LayoutInflater.from(itemView.getContext()).inflate(this.getLeftMenuLayout(), this.flLeftMenu, true);
            this.initLeftMenuItem(this.flLeftMenu);
            this.swipeItemLayout.getMenus().put(1, this.flLeftMenu);
        }

        if (this.getRightMenuLayout() != -1) {
            LayoutInflater.from(itemView.getContext()).inflate(this.getRightMenuLayout(), this.flRightMenu, true);
            this.initRightMenuItem(this.flRightMenu);
            this.swipeItemLayout.getMenus().put(2, this.flRightMenu);
        }

        LayoutInflater.from(itemView.getContext()).inflate(this.getContentLayout(), this.flContent, true);
        this.initContentItem(this.flContent);
        this.initItem(this.swipeItemLayout);
    }

    public LSwipeItemLayout getSwipeItemLayout() {
        return this.swipeItemLayout;
    }

    public int getLeftMenuLayout() {
        return -1;
    }

    public int getRightMenuLayout() {
        return -1;
    }

    public abstract int getContentLayout();

    public void initLeftMenuItem(FrameLayout flLeftMenu) {
    }

    public void initRightMenuItem(FrameLayout flRightMenu) {
    }

    public void initContentItem(FrameLayout flContent) {

    }

    public abstract void initItem(FrameLayout var1);
}