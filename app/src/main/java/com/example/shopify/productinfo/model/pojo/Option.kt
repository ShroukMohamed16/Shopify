package com.example.shopify.productinfo.model.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Option {
    @SerializedName("id")
    @Expose
    var id: Long? = null

    @SerializedName("product_id")
    @Expose
    var productId: Long? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("position")
    @Expose
    var position: Int? = null

    @SerializedName("values")
    @Expose
    var values: List<String>? = null
}