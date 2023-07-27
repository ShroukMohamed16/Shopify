package com.example.shopify.productinfo.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRemoteSource {
    suspend fun getProductByID(id:String) : ProductResponse
    suspend fun getDraftOrderByID(id:Long):DraftOrderResponse
    suspend fun modifyDraftOrder(id: Long,draftOrder: DraftOrderResponse):DraftOrderResponse
}