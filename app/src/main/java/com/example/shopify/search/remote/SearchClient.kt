package com.example.shopify.search.remote

import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.search.model.pojo.ProductListResponse

class SearchClient:SearchRemoteSource {
    val searchService: SearchService by lazy {
        RetrofitHelper.retrofitInstance.create(SearchService::class.java)
    }

    override suspend fun getProduct(): ProductListResponse {
        return searchService.getProducts()
    }
}