package com.example.shopify.productinfo.remote

import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

interface ProductDetailsRemoteSource {
    suspend fun getProductByID(id:String) : ProductResponse
}