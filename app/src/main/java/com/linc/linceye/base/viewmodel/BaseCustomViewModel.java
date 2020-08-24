package com.linc.linceye.base.viewmodel;

import java.io.Serializable;
/**
 * <用于各个组件之间公用的 契约类> <功能详细描述>
 *
 * @author linc
 * @version 2020/8/18
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public abstract class BaseCustomViewModel implements Serializable {
    public abstract int type();
}
