package com.example.shopify.Payment.Model.Repository

import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder
import com.example.shopify.Payment.Remote.PaymentRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class PaymentRepository (var paymentRemote : PaymentRemoteSource) : PaymentRepositoryInterface {

    companion object {
        private var instance: PaymentRepository? = null
        fun getInstance(remote: PaymentRemoteSource): PaymentRepository {
            return instance ?: synchronized(this) {
                val Instance = PaymentRepository( remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun postOrder(id : Long):Flow<Boolean> {
        return flowOf(paymentRemote.postOrder(id))
    }
}