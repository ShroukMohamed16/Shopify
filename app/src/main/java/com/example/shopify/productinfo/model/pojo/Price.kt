package com.example.shopify.productinfo.model.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Price {
    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("currency_code")
    @Expose
    var currencyCode: String? = null
}