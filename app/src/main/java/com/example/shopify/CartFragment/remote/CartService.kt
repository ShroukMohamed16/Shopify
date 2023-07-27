package com.example.shopify.CartFragment.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface CartService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("draft_orders/{id}.json")
    suspend fun getCartDraftOrderById(@Path("id") id:String) : DraftOrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("draft_orders/{id}.json")
    suspend fun editCartDraftOrderById(@Path("id") id:String,@Body draftOrder: DraftOrderResponse) : DraftOrderResponse
}