package com.example.shopify.productinfo.remote

import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.homeFragment.Remote.BrandsService
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.flow.Flow

class ProductDetailsClient:ProductDetailsRemoteSource {

    val productDetailsService: ProductDetailsService by lazy {
        RetrofitHelper.retrofitInstance.create(ProductDetailsService::class.java)
    }

    override suspend fun getProductByID(id: String): ProductResponse {
        return productDetailsService.getProductById(id)
    }
}