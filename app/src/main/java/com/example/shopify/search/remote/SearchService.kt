package com.example.shopify.search.remote

import com.example.shopify.search.model.pojo.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface SearchService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("products.json")
    suspend fun getProducts() : ProductListResponse
}