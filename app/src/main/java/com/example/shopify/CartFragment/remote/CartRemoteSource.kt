package com.example.shopify.CartFragment.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.Path

interface CartRemoteSource {

    suspend fun getCartDraftOrderById( id:String) : Flow<DraftOrderResponse>
    suspend fun editCartDraftOrderById( id:String, draftOrder: DraftOrderResponse) : Flow<DraftOrderResponse>

}