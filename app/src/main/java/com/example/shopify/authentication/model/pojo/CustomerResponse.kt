package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("customer")
    @Expose
    var customer: Customer
)