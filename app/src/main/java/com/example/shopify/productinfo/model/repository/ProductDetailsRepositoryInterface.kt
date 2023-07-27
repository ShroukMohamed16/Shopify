package com.example.shopify.productinfo.model.repository

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepositoryInterface {
    suspend fun getProductByID(id:String):Flow<ProductResponse>
    suspend fun getDraftOrderById(id:String):Flow<DraftOrderResponse>
    suspend fun modifyDraftOrder(id:String,draftOrder: DraftOrderResponse):Flow<DraftOrderResponse>
}