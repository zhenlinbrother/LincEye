package com.linc.linceye.bean;

import com.linc.linceye.mvvm.home.nominate.bean.ItemListBean;

import java.util.List;

public class RespData<T> {

    private String itemList;
    private int count;
    private int total;
    private String nextPageUrl;
    private boolean adExist;

    public String getData() {
        return itemList;
    }

    public void setData(String data) {
        this.itemList = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public boolean isAdExist() {
        return adExist;
    }

    public void setAdExist(boolean adExist) {
        this.adExist = adExist;
    }
}
