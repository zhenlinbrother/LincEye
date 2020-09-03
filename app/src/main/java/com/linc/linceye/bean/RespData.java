package com.linc.linceye.bean;

import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;

import java.util.List;

public class RespData<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
