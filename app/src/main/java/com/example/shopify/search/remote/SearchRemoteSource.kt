package com.example.shopify.search.remote

import com.example.shopify.search.model.pojo.ProductListResponse

interface SearchRemoteSource {
    suspend fun getProduct() : ProductListResponse

}