package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerResponse(var customerData: Customer){
    @SerializedName("customer")
    @Expose
    var customer: Customer? = customerData
}