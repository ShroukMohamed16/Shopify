package com.example.shopify.productinfo.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface ProductDetailsService {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id:String) : ProductResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("draft_orders/{id}.json")
    suspend fun getFavDraftOrderById(@Path("id") id:String) : DraftOrderResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("draft_orders/{id}.json")
    suspend fun modifyFavDraftOrder(@Path("id") id:String,@Body draft_order: DraftOrderResponse) : DraftOrderResponse



}