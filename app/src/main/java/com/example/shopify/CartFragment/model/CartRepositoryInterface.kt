package com.example.shopify.CartFragment.model

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface CartRepositoryInterface {

    suspend fun getCartDraftOrderById( id:String) : Flow<DraftOrderResponse>
    suspend fun editCartDraftOrderById( id:String,  draftOrder: DraftOrderResponse) : Flow<DraftOrderResponse>

}