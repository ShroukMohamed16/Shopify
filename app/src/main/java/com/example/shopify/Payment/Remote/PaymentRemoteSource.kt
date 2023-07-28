package com.example.shopify.Payment.Remote

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.OrderData
import com.example.shopify.Payment.Model.DataClass.PostOrder

interface PaymentRemoteSource {
    suspend fun createOrder(orderData: OrderData) :OrderResponse
}