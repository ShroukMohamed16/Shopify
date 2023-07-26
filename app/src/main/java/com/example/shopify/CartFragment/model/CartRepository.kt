package com.example.shopify.CartFragment.model

import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepository
import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepositoryInterface
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.AllOrdersFragment.Remote.AllOrderRemoteSource
import com.example.shopify.CartFragment.remote.CartRemoteSource
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

class CartRepository(private var remoteSource: CartRemoteSource) :
    CartRepositoryInterface {
    companion object {
        private var instance: CartRepository? = null
        fun getInstance(remoteSource: CartRemoteSource): CartRepository {
            return instance ?: synchronized(this) {
                val Instance = CartRepository(remoteSource)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getCartDraftOrderById(id: String): Flow<DraftOrderResponse> {
        return remoteSource.getCartDraftOrderById(id)
    }

    override suspend fun editCartDraftOrderById(
        id: String,
        draftOrder: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return remoteSource.editCartDraftOrderById(id, draftOrder)
    }

}