package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.SerializedName

class CustomerListResponse{

    @SerializedName("customers")
    private var customers: List<Customer?>? = null

    fun getCustomers(): List<Customer?>? {
        return customers
    }

    fun setCustomers(customers: List<Customer?>?) {
        this.customers = customers
    }

}