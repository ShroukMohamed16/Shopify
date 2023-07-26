package com.example.shopify.CartFragment.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.Remote.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.http.Path

class CartClient  private constructor(): CartRemoteSource {
    val cartService: CartService by lazy {
        RetrofitHelper.retrofitInstance.create(CartService::class.java)
    }
    companion object {
        private var instance: CartClient? = null
        fun getInstance(): CartClient {
            return instance ?: synchronized(this) {
                val temp = CartClient()
                instance = temp
                temp
            }
        }
    }

    override suspend fun getCartDraftOrderById(id: String): Flow<DraftOrderResponse> {
        return flowOf(cartService.getCartDraftOrderById(id))
    }

    override suspend fun editCartDraftOrderById(
        id: String,
        draftOrder: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return flowOf(cartService.editCartDraftOrderById(id,draftOrder))
    }

}