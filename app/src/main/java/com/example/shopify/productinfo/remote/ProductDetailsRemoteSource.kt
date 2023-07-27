package com.example.shopify.productinfo.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRemoteSource {
    suspend fun getProductByID(id:String) : ProductResponse
    suspend fun getDraftOrderByID(id:String):DraftOrderResponse
    suspend fun modifyDraftOrder(id: String,draftOrder: DraftOrderResponse):DraftOrderResponse
}