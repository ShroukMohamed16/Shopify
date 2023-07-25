package com.example.shopify.Payment.Remote

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder
import com.example.shopify.base.Remote.RetrofitHelper

class PaymentClient : PaymentRemoteSource {
    val payment_Service: PaymentService by lazy {
        RetrofitHelper.retrofitInstance.create(PaymentService::class.java)
    }

    override suspend fun postOrder(id : Long): OrderResponse {
        return payment_Service.createOrder(id)
    }
}