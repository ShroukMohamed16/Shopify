package com.example.shopify.search.model.repository

import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsRemoteSource
import com.example.shopify.search.model.pojo.ProductListResponse
import com.example.shopify.search.remote.SearchRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SearchRepository(private var searchRemoteSource: SearchRemoteSource):SearchRepositoryInterface {

    companion object {
        private var instance: SearchRepository? = null
        fun getInstance(searchRemoteSource: SearchRemoteSource): SearchRepository {
            return instance ?: synchronized(this) {
                val Instance = SearchRepository(searchRemoteSource)
                instance = Instance
                Instance
            }
        }
    }
    override suspend fun getProductsFromAPI(): Flow<ProductListResponse> {
        return flowOf(searchRemoteSource.getProduct())
    }
}