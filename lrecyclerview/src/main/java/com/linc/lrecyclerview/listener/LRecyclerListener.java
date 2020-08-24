package com.linc.lrecyclerview.listener;

import android.view.MotionEvent;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface LRecyclerListener {

    boolean onTouch(MotionEvent var1, float var2);

    boolean onUp(MotionEvent var1);
}
