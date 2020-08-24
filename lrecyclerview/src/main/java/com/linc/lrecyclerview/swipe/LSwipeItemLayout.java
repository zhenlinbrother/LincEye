package com.linc.lrecyclerview.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <侧滑 layout> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LSwipeItemLayout extends FrameLayout {
    public String TAG;
    private ViewDragHelper mDragHelper;
    private int mTouchSlop;
    private int mVelocity;
    private float mDownX;
    private float mDownY;
    private boolean mIsDragged;
    private boolean mSwipeEnable;
    private final Rect mTouchRect;
    private View mCurrentMenu;
    private boolean mIsOpen;
    private LinkedHashMap<Integer, View> mMenus;
    private List<SwipeListener> mListeners;

    public LSwipeItemLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public LSwipeItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LSwipeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.TAG = this.getClass().getSimpleName();
        this.mSwipeEnable = true;
        this.mMenus = new LinkedHashMap();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        this.mDragHelper = ViewDragHelper.create(this, new LSwipeItemLayout.DragCallBack());
        this.mTouchRect = new Rect();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.updateMenu();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            if (this.isCloseAnimating()) {
                return false;
            }

            if (this.mIsOpen && this.isTouchContent((int)ev.getX(), (int)ev.getY())) {
                this.close();
                return false;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!this.mSwipeEnable) {
            return false;
        } else {
            int action = ev.getAction();
            switch(action) {
                case 0:
                    this.mIsDragged = false;
                    this.mDownX = ev.getX();
                    this.mDownY = ev.getY();
                    break;
                case 1:
                case 3:
                    if (this.mIsDragged) {
                        this.mDragHelper.processTouchEvent(ev);
                        this.mIsDragged = false;
                    }
                    break;
                case 2:
                    this.checkCanDragged(ev);
                    break;
                default:
                    if (this.mIsDragged) {
                        this.mDragHelper.processTouchEvent(ev);
                    }
            }

            return this.mIsDragged || super.onInterceptTouchEvent(ev);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (!this.mSwipeEnable) {
            return super.onTouchEvent(ev);
        } else {
            int action = ev.getAction();
            switch(action) {
                case 0:
                    this.mIsDragged = false;
                    this.mDownX = ev.getX();
                    this.mDownY = ev.getY();
                    break;
                case 1:
                case 3:
                    if (this.mIsDragged || this.mIsOpen) {
                        this.mDragHelper.processTouchEvent(ev);
                        ev.setAction(3);
                        this.mIsDragged = false;
                    }
                    break;
                case 2:
                    boolean beforeCheckDrag = this.mIsDragged;
                    this.checkCanDragged(ev);
                    if (this.mIsDragged) {
                        this.mDragHelper.processTouchEvent(ev);
                    }

                    if (!beforeCheckDrag && this.mIsDragged) {
                        MotionEvent obtain = MotionEvent.obtain(ev);
                        obtain.setAction(3);
                        super.onTouchEvent(obtain);
                    }
                    break;
                default:
                    if (this.mIsDragged) {
                        this.mDragHelper.processTouchEvent(ev);
                    }
            }

            return this.mIsDragged || super.onTouchEvent(ev) || !this.isClickable() && this.mMenus.size() > 0;
        }
    }

    private void checkCanDragged(MotionEvent ev) {
        if (!this.mIsDragged) {
            float dx = ev.getX() - this.mDownX;
            float dy = ev.getY() - this.mDownY;
            boolean isRightDrag = dx > (float)this.mTouchSlop && Math.abs(dx) > Math.abs(dy);
            boolean isLeftDrag = dx < (float)(-this.mTouchSlop) && Math.abs(dx) > Math.abs(dy);
            if (this.mIsOpen) {
                int downX = (int)this.mDownX;
                int downY = (int)this.mDownY;
                if (this.isTouchContent(downX, downY)) {
                    this.mIsDragged = true;
                } else if (this.isTouchMenu(downX, downY)) {
                    this.mIsDragged = this.isLeftMenu() && isLeftDrag || this.isRightMenu() && isRightDrag;
                }
            } else if (isRightDrag) {
                this.mCurrentMenu = (View)this.mMenus.get(1);
                this.mIsDragged = this.mCurrentMenu != null;
            } else if (isLeftDrag) {
                this.mCurrentMenu = (View)this.mMenus.get(2);
                this.mIsDragged = this.mCurrentMenu != null;
            }

            if (this.mIsDragged) {
                MotionEvent obtain = MotionEvent.obtain(ev);
                obtain.setAction(0);
                this.mDragHelper.processTouchEvent(obtain);
                if (this.getParent() != null) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }

        }
    }

    public LinkedHashMap<Integer, View> getMenus() {
        return this.mMenus;
    }

    public void setSwipeEnable(boolean enable) {
        this.mSwipeEnable = enable;
    }

    public boolean isSwipeEnable() {
        return this.mSwipeEnable;
    }

    public View getContentView() {
        return this.getChildAt(this.getChildCount() - 1);
    }

    private boolean isLeftMenu() {
        return this.mCurrentMenu != null && this.mCurrentMenu == this.mMenus.get(1);
    }

    private boolean isRightMenu() {
        return this.mCurrentMenu != null && this.mCurrentMenu == this.mMenus.get(2);
    }

    public boolean isTouchMenu(int x, int y) {
        if (this.mCurrentMenu == null) {
            return false;
        } else {
            this.mCurrentMenu.getHitRect(this.mTouchRect);
            return this.mTouchRect.contains(x, y);
        }
    }

    public boolean isTouchContent(int x, int y) {
        View contentView = this.getContentView();
        if (contentView == null) {
            return false;
        } else {
            contentView.getHitRect(this.mTouchRect);
            return this.mTouchRect.contains(x, y);
        }
    }

    public void close() {
        if (this.mCurrentMenu == null) {
            this.mIsOpen = false;
        } else {
            this.mDragHelper.smoothSlideViewTo(this.getContentView(), this.getPaddingLeft(), this.getPaddingTop());
            this.mIsOpen = false;
            if (this.mListeners != null) {
                int listenerCount = this.mListeners.size();

                for(int i = listenerCount - 1; i >= 0; --i) {
                    ((LSwipeItemLayout.SwipeListener)this.mListeners.get(i)).onSwipeClose(this);
                }
            }

            this.invalidate();
        }
    }

    public void open() {
        if (this.mCurrentMenu == null) {
            this.mIsOpen = false;
        } else {
            if (this.isLeftMenu()) {
                this.mDragHelper.smoothSlideViewTo(this.getContentView(), this.mCurrentMenu.getWidth(), this.getPaddingTop());
            } else if (this.isRightMenu()) {
                this.mDragHelper.smoothSlideViewTo(this.getContentView(), -this.mCurrentMenu.getWidth(), this.getPaddingTop());
            }

            this.mIsOpen = true;
            if (this.mListeners != null) {
                int listenerCount = this.mListeners.size();

                for(int i = listenerCount - 1; i >= 0; --i) {
                    ((LSwipeItemLayout.SwipeListener)this.mListeners.get(i)).onSwipeOpen(this);
                }
            }

            this.invalidate();
        }
    }

    public boolean isOpen() {
        return this.mIsOpen;
    }

    private boolean isOpenAnimating() {
        if (this.mCurrentMenu != null) {
            int contentLeft = this.getContentView().getLeft();
            int menuWidth = this.mCurrentMenu.getWidth();
            if (this.mIsOpen && (this.isLeftMenu() && contentLeft < menuWidth || this.isRightMenu() && -contentLeft < menuWidth)) {
                return true;
            }
        }

        return false;
    }

    private boolean isCloseAnimating() {
        if (this.mCurrentMenu != null) {
            int contentLeft = this.getContentView().getLeft();
            if (!this.mIsOpen && (this.isLeftMenu() && contentLeft > 0 || this.isRightMenu() && contentLeft < 0)) {
                return true;
            }
        }

        return false;
    }

    private void updateMenu() {
        View contentView = this.getContentView();
        if (contentView != null) {
            int contentLeft = contentView.getLeft();
            if (contentLeft == 0) {
                Iterator var3 = this.mMenus.values().iterator();

                while(var3.hasNext()) {
                    View view = (View)var3.next();
                    view.setVisibility(INVISIBLE);
                }
            } else if (this.mCurrentMenu != null) {
                this.mCurrentMenu.setVisibility(VISIBLE);
            }
        }

    }

    public void addSwipeListener(LSwipeItemLayout.SwipeListener listener) {
        if (listener != null) {
            if (this.mListeners == null) {
                this.mListeners = new ArrayList();
            }

            this.mListeners.add(listener);
        }
    }

    public void removeSwipeListener(LSwipeItemLayout.SwipeListener listener) {
        if (listener != null) {
            if (this.mListeners != null) {
                this.mListeners.remove(listener);
            }
        }
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    public interface SwipeListener {
        void onSwipeOpen(LSwipeItemLayout var1);

        void onSwipeClose(LSwipeItemLayout var1);
    }

    private class DragCallBack extends ViewDragHelper.Callback {
        private DragCallBack() {
        }

        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return child == LSwipeItemLayout.this.getContentView() || LSwipeItemLayout.this.mMenus.containsValue(child);
        }

        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (child == LSwipeItemLayout.this.getContentView()) {
                if (LSwipeItemLayout.this.isRightMenu()) {
                    return left > 0 ? 0 : (left < -LSwipeItemLayout.this.mCurrentMenu.getWidth() ? -LSwipeItemLayout.this.mCurrentMenu.getWidth() : left);
                }

                if (LSwipeItemLayout.this.isLeftMenu()) {
                    return left > LSwipeItemLayout.this.mCurrentMenu.getWidth() ? LSwipeItemLayout.this.mCurrentMenu.getWidth() : (left < 0 ? 0 : left);
                }
            } else {
                View contentView;
                int newLeft;
                if (LSwipeItemLayout.this.isRightMenu()) {
                    contentView = LSwipeItemLayout.this.getContentView();
                    newLeft = contentView.getLeft() + dx;
                    if (newLeft > 0) {
                        newLeft = 0;
                    } else if (newLeft < -child.getWidth()) {
                        newLeft = -child.getWidth();
                    }

                    contentView.layout(newLeft, contentView.getTop(), newLeft + contentView.getWidth(), contentView.getBottom());
                    return child.getLeft();
                }

                if (LSwipeItemLayout.this.isLeftMenu()) {
                    contentView = LSwipeItemLayout.this.getContentView();
                    newLeft = contentView.getLeft() + dx;
                    if (newLeft < 0) {
                        newLeft = 0;
                    } else if (newLeft > child.getWidth()) {
                        newLeft = child.getWidth();
                    }

                    contentView.layout(newLeft, contentView.getTop(), newLeft + contentView.getWidth(), contentView.getBottom());
                    return child.getLeft();
                }
            }

            return 0;
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            LSwipeItemLayout.this.updateMenu();
        }

        public void onViewReleased(@NonNull View releasedChild, float xVel, float yVel) {
            if (LSwipeItemLayout.this.isLeftMenu()) {
                if (xVel > (float)LSwipeItemLayout.this.mVelocity) {
                    LSwipeItemLayout.this.open();
                } else if (xVel < (float)(-LSwipeItemLayout.this.mVelocity)) {
                    LSwipeItemLayout.this.close();
                } else if (LSwipeItemLayout.this.getContentView().getLeft() > LSwipeItemLayout.this.mCurrentMenu.getWidth() / 3 * 2) {
                    LSwipeItemLayout.this.open();
                } else {
                    LSwipeItemLayout.this.close();
                }
            } else if (LSwipeItemLayout.this.isRightMenu()) {
                if (xVel < (float)(-LSwipeItemLayout.this.mVelocity)) {
                    LSwipeItemLayout.this.open();
                } else if (xVel > (float)LSwipeItemLayout.this.mVelocity) {
                    LSwipeItemLayout.this.close();
                } else if (LSwipeItemLayout.this.getContentView().getLeft() < -LSwipeItemLayout.this.mCurrentMenu.getWidth() / 3 * 2) {
                    LSwipeItemLayout.this.open();
                } else {
                    LSwipeItemLayout.this.close();
                }
            }

        }
    }
}
