package com.example.shopify.Payment.Remote

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder
import retrofit2.http.*

interface PaymentService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("draft_orders/{draft_order_id}/complete.json")
    suspend fun createOrder(@Path(value = "draft_order_id")draftOrderId:Long): OrderResponse
}