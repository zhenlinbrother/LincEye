package com.linc.linceye.mvvm.home.nominate.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemListBean {
//    @SerializedName("itemList")
//    private List<ItemInfoBean> itemList;
//    @SerializedName("count")
//    private int count;
//    @SerializedName("total")
//    private int total;
//    @SerializedName("nextPageUrl")
//    private String nextPageUrl;
//    @SerializedName("adExist")
//    private boolean adExist;
//
//    public List<ItemInfoBean> getItemList() {
//        return itemList;
//    }
//
//    public void setItemList(List<ItemInfoBean> itemList) {
//        this.itemList = itemList;
//    }
//
//    public int getCount() {
//        return count;
//    }
//
//    public void setCount(int count) {
//        this.count = count;
//    }
//
//    public int getTotal() {
//        return total;
//    }
//
//    public void setTotal(int total) {
//        this.total = total;
//    }
//
//    public String getNextPageUrl() {
//        return nextPageUrl;
//    }
//
//    public void setNextPageUrl(String nextPageUrl) {
//        this.nextPageUrl = nextPageUrl;
//    }
//
//    public boolean isAdExist() {
//        return adExist;
//    }
//
//    public void setAdExist(boolean adExist) {
//        this.adExist = adExist;
//    }
    private String type;
    private SquareCardCollectionBean data;
    private Object tag;
    private int id;
    private int adIndex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SquareCardCollectionBean getData() {
        return data;
    }

    public void setData(SquareCardCollectionBean data) {
        this.data = data;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdIndex() {
        return adIndex;
    }

    public void setAdIndex(int adIndex) {
        this.adIndex = adIndex;
    }

}
