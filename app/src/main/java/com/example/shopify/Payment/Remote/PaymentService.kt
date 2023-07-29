package com.example.shopify.Payment.Remote

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.OrderData
import com.example.shopify.Payment.Model.DataClass.PostOrder
import retrofit2.http.*

interface PaymentService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("orders.json")
    suspend fun createOrder(@Body order: OrderData):OrderResponse
}