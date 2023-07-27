package com.example.shopify.Payment.Remote

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder

interface PaymentRemoteSource {
    suspend fun postOrder(id : Long): Boolean
}