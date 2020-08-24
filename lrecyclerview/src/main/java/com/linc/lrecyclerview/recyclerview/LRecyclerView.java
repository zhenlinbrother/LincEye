package com.linc.lrecyclerview.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.linc.lrecyclerview.adapter.LRefreshAndLoadMoreAdapter;
import com.linc.lrecyclerview.listener.IStick;
import com.linc.lrecyclerview.listener.LRecyclerListener;
import com.linc.lrecyclerview.swipe.LSwipeItemLayout;
import com.linc.lrecyclerview.utils.LogUtils;
import com.linc.lrecyclerview.view.base.IBaseLoadMoreView;
import com.linc.lrecyclerview.view.base.IBaseRefreshLoadView;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_OUTSIDE;
import static android.view.MotionEvent.ACTION_UP;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LRecyclerView extends RecyclerView {

    private static final String TAG = LRecyclerView.class.getSimpleName();
    private float mLastY;
    private static final int DRAG_FACTOR = 1;
    private int mRefreshViewPos;
    private final Rect mFrame;
    private boolean mIsTouching;
    private boolean isScrolling;
    private LRecyclerListener mListener;

    public LRecyclerView(@NonNull Context context) {
        this(context, null, 0);
    }

    public LRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLastY = -1.0f;
        this.mRefreshViewPos = 0;
        this.mIsTouching = false;
        this.isScrolling = false;
        this.addOnScrollListener(new LRecyclerView.ScrollerListener());
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.mFrame = new Rect();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == ACTION_DOWN){
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            View openItemView = this.findOpenItem();
            if (openItemView != null && openItemView != this.getTouchItem(x, y)){
                LSwipeItemLayout swipeItemLayout = this.findSwipeItemLayout(openItemView);
                if (swipeItemLayout != null){
                    swipeItemLayout.close();
                    return false;
                }
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (this.mLastY == -1.0f){
            this.mLastY = e.getRawY();
        }

        switch (e.getActionMasked()){
            case ACTION_DOWN:
                this.mLastY = e.getRawY();
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
            case ACTION_OUTSIDE:
                this.mLastY = -1.0f;
                this.handleRefreshLoad();
                this.handleLoadMore();
                if (this.mListener != null){
                    this.mListener.onUp(e);
                }

                this.mIsTouching = false;
                if (this.isScrollStick()){
                    return true;
                }
                break;
            case ACTION_MOVE:
                this.mIsTouching = true;
                int deltaY = (int) (e.getRawY() - this.mLastY);
                LogUtils.i("loadMoreView: [rawY: " + e.getRawY() + "; " + "lastY: " + this.mLastY + "; " + "deltaY: " + deltaY + "]");
                this.mLastY = e.getRawY();
                if (this.handleTouch(e, (float)deltaY)){
                    e.setAction(ACTION_DOWN);
                    super.onTouchEvent(e);
                    return false;
                }

                int visibleHeight;
                if (this.getRefreshLoadView() != null && this.isScrolledTop()){
                    visibleHeight = this.getRefreshVisibleHeight();
                    if (visibleHeight != -1){
                        this.getRefreshLoadView().onMove(visibleHeight, deltaY >> 1);
                    }

                    if (visibleHeight > 0 && this.getRefreshLoadView().getCurState() < 8){
                        e.setAction(ACTION_DOWN);
                        super.onTouchEvent(e);
                        return false;
                    }
                }

                if (this.getLoadMoreView() != null && this.isScrolledBottom()){
                    visibleHeight = this.getLoadMoreVisibleHeight();
                    if (visibleHeight != -1){
                        this.getLoadMoreView().onMove(visibleHeight, (float)(-deltaY >> 1));

                        return super.onTouchEvent(e);
                    }
                }
        }
        return super.onTouchEvent(e);
    }

    //处理触碰事件（可在此制作黏性标题栏事件)
    private boolean handleTouch(MotionEvent e, float deltaY){
        if (this.mListener != null){
            LogUtils.i("mListener.onTouch: [rawY: " + e.getRawY() + "; " + "lastY: " + this.mLastY + "; " + "deltaY: " + deltaY + "]");
            this.mLastY = e.getRawY();
            return this.mListener.onTouch(e, deltaY);
        } else {
            return false;
        }
    }

    //判断是否滑动到顶部
    //todo 可以继续优化当LayoutManager 为网格布局或者瀑布流布局时
    private boolean isScrolledTop(){
        return this.getChildCount() > 1
                && this.getChildAt(0) instanceof IBaseRefreshLoadView
                && ((LinearLayoutManager)this.getLayoutManager()).findFirstVisibleItemPosition() <= 1
                && this.getChildAt(1).getY() >= 0.0f;
    }

    //判断是否滑动到底部
    //todo 可以继续优化当LayoutManager 为网格布局或者瀑布流布局时
    private boolean isScrolledBottom() {
        int itemTotal;
        if (this.getRefreshLoadView() != null && this.getRefreshLoadView().getCurState() == 8) {
            itemTotal = this.getAdapter().getItemCount() - 2;
        } else {
            itemTotal = this.getAdapter().getItemCount() - 3;
        }

        return this.getLayoutManager() instanceof LinearLayoutManager
                && ((LinearLayoutManager)this.getLayoutManager()).findLastCompletelyVisibleItemPosition() >= itemTotal;
    }

    public LRecyclerListener getListener() {
        return mListener;
    }

    public void setListener(LRecyclerListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    private View findOpenItem(){
        int childCount = this.getChildCount();

        for (int i = 0; i < childCount; i++){
            LSwipeItemLayout lSwipeItemLayout = this.findSwipeItemLayout(this.getChildAt(i));
            if (lSwipeItemLayout != null && lSwipeItemLayout.isOpen()){
                return this.getChildAt(i);
            }
        }

        return null;
    }

    @Nullable
    private LSwipeItemLayout findSwipeItemLayout(View view){
        if (view instanceof LSwipeItemLayout){
            return (LSwipeItemLayout) view;
        } else {
            if (view instanceof ViewGroup){
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();

                for (int i = 0; i < count; i++){
                    LSwipeItemLayout swipeItemLayout = this.findSwipeItemLayout(group.getChildAt(i));
                    if (swipeItemLayout != null){
                        return swipeItemLayout;
                    }
                }
            }
        }

        return null;
    }
    private IBaseRefreshLoadView getRefreshLoadView(){
        if (this.getAdapter() instanceof LRefreshAndLoadMoreAdapter){
            LRefreshAndLoadMoreAdapter lAdapter = (LRefreshAndLoadMoreAdapter) this.getAdapter();
            return lAdapter.getRefreshLoadView();
        } else {
            return null;
        }
    }

    private IBaseLoadMoreView getLoadMoreView(){
        if (this.getAdapter() instanceof LRefreshAndLoadMoreAdapter){
            LRefreshAndLoadMoreAdapter lAdapter = (LRefreshAndLoadMoreAdapter) this.getAdapter();
            return lAdapter.getLoadMoreView();
        } else {
            return null;
        }
    }

    /**
     * 判断是否正在处理下拉刷新
     * @return
     */
    private boolean handleRefreshLoad(){
        if (this.getRefreshLoadView() == null){
            return false;
        } else {
            int visibleHeight = this.getRefreshVisibleHeight();
            if (visibleHeight == -1){
                return false;
            } else if (this.getRefreshLoadView().releaseAction(visibleHeight)){
                if (this.getRefreshLoadView().getOnRefreshListener() != null){
                    this.getRefreshLoadView().getOnRefreshListener().onRefreshing();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private boolean handleLoadMore(){
        if (this.getLoadMoreView() == null){
            return false;
        } else {
            int visibleHeight = this.getLoadMoreVisibleHeight();
            if (visibleHeight == -1){
                return false;
            } else if (this.getLoadMoreView().releaseAction(visibleHeight)){
                if (this.getLoadMoreView().getOnLoadMoreListener() != null){
                    this.getLoadMoreView().getOnLoadMoreListener().onLoading();
                }

                return true;
            } else {
                return false;
            }
        }
    }

    private int getLoadMoreVisibleHeight(){
        if (this.getLoadMoreView() == null){
            return -1;
        }

        int childCount = this.getLayoutManager().getChildCount();
        if (childCount <= 0){
            return -1;
        }

        View view = this.getLayoutManager().getChildAt(childCount - 1);
        if (view == null){
            return  -1;
        } else if (view != this.getLoadMoreView()){
            return -1;
        } else {
            int layoutHeight = this.getLayoutManager().getHeight();
            int top = view.getTop();
            return layoutHeight - top;
        }
    }

    /**
     * 获取下拉刷新视图的可见高度
     * @return
     */
    private int getRefreshVisibleHeight(){
        if (this.getRefreshLoadView() == null){
            return -1;
        } else {
            int childCount = this.getLayoutManager().getChildCount();
            if (childCount <= 0){
                return -1;
            } else {
                View view = this.getLayoutManager().getChildAt(this.mRefreshViewPos);
                if (view == null){
                    return -1;
                } else if (view != this.getRefreshLoadView()){
                    return -1;
                } else {
                    int top = view.getTop();
                    int bottom = view.getBottom();
                    return bottom - top;
                }
            }
        }
    }

    private boolean isScrollStick(){
        if (!this.isScrolling){
            return false;
        } else {
            View theFirstView = this.getChildAt(0);
            if (theFirstView == null){
                return false;
            } else {
                ViewHolder childViewHolder = this.getChildViewHolder(theFirstView);
                if (childViewHolder == null){
                    return false;
                } else if (!(childViewHolder instanceof IStick)){
                    return false;
                } else {
                    boolean isInAction = false;
                    if (this.handleRefreshLoad()){
                        isInAction = true;
                    }

                    if (this.handleLoadMore()){
                        isInAction = true;
                    }

                    if (isInAction){
                        return false;
                    } else {
                        this.stopScroll();
                        float y = theFirstView.getY();
                        int height = theFirstView.getHeight();
                        boolean isShowAll = Math.abs(y) > (float)(height / 2);
                        float offset = isShowAll ? (float)height + y : y;
                        this.smoothScrollBy(0, (int)offset);
                        return true;
                    }
                }
            }
        }
    }

    @Nullable
    private View getTouchItem(int x, int y){
        for (int i = 0; i < this.getChildCount(); i++){
            View child = this.getChildAt(i);
            if (child.getVisibility() == VISIBLE){
                child.getHitRect(this.mFrame);
                if (this.mFrame.contains(x, y)){
                    return child;
                }
            }
        }

        return null;
    }

    /**
     * 滑动监听器
     */
    private class ScrollerListener extends OnScrollListener {
        public ScrollerListener() {
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            switch (newState){
                case 0:
                    LRecyclerView.this.isScrolling = false;
                    break;
                case 1:
                case 2:
                    LRecyclerView.this.isScrolling = true;
                    break;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (!LRecyclerView.this.mIsTouching){
                LRecyclerView.this.isScrollStick();
            }
        }
    }
}
