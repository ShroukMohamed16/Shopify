package com.example.shopify.AllOrdersFragment.Remote

import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface AllOrderService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("orders.json")
   suspend fun getAllOrdersOfUser(@Query("email") email:String) : OrderResponse
}