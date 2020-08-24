package com.linc.lrecyclerview.config;


import com.linc.lrecyclerview.R;

/**
 * <一句话解释功能> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/11
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class LRecyclerConfig {
    public static int SWIPE_LAYOUT;

    public static final int HEAD = 11256065;
    public static final int FOOT = 11256067;
    public static final int ANIM_DURATION = 500;

    static {
        SWIPE_LAYOUT = R.layout.l_swipe_wrapper;
    }
}
