package com.example.shopify.productinfo.model.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ProductResponse {
    @SerializedName("product")
    @Expose
    var product: Product? = null
}