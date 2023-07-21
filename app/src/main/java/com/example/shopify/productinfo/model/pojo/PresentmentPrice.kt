package com.example.shopify.productinfo.model.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.example.shopify.productinfo.model.pojo.Price

class PresentmentPrice {
    @SerializedName("price")
    @Expose
    var price: Price? = null

    @SerializedName("compare_at_price")
    @Expose
    var compareAtPrice: Any? = null
}