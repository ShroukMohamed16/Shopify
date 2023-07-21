package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.Customer
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("customers.json")
    suspend fun addNewCustomer(customer: Customer)
}