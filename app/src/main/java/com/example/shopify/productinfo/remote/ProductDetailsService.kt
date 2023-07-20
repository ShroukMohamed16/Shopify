package com.example.shopify.productinfo.remote

import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ProductDetailsService {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id:String) : ProductResponse

}