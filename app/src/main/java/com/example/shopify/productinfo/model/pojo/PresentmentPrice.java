package com.example.shopify.productinfo.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PresentmentPrice {

    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("compare_at_price")
    @Expose
    private Object compareAtPrice;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Object getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(Object compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

}
