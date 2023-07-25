package com.example.shopify.search.model.repository

import com.example.shopify.search.model.pojo.ProductListResponse
import kotlinx.coroutines.flow.Flow

interface SearchRepositoryInterface {

    suspend fun getProductsFromAPI():Flow<ProductListResponse>
}