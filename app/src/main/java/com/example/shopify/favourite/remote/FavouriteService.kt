package com.example.shopify.favourite.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.search.model.pojo.ProductListResponse
import retrofit2.http.*

interface FavouriteService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("draft_orders/{id}.json")
    suspend fun getFavDraftOrderById(@Path("id") id:Long) : DraftOrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("draft_orders/{id}.json")
    suspend fun modifyFavDraftOrder(@Path("id") id:Long,@Body draft_order: DraftOrderResponse) : DraftOrderResponse



}