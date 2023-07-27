package com.example.shopify.productinfo.remote

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
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

    override suspend fun getDraftOrderByID(id: String): DraftOrderResponse {
        return productDetailsService.getFavDraftOrderById(id)
    }

    override suspend fun modifyDraftOrder(id: String, draftOrder: DraftOrderResponse): DraftOrderResponse {
        return productDetailsService.modifyFavDraftOrder(id,draftOrder)
    }
}