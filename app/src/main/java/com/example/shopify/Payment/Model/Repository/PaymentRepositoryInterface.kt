package com.example.shopify.Payment.Model.Repository

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder
import kotlinx.coroutines.flow.Flow

interface PaymentRepositoryInterface {
    suspend fun postOrder(id : Long) :Flow<Boolean>
}