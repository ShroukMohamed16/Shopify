package com.example.shopify.productinfo.model.repository

import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRepositoryInterface {
    suspend fun getProductByID(id:String):Flow<ProductResponse>
}