package com.example.shopify.productinfo.model.repository

import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Remote.BrandsRemoteSource
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.productinfo.remote.ProductDetailsRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductDetailsRepository(private val productDetailsRemoteSource: ProductDetailsRemoteSource):ProductDetailsRepositoryInterface {

    companion object {
        private var instance: ProductDetailsRepository? = null
        fun getInstance(remote: ProductDetailsRemoteSource): ProductDetailsRepository {
            return instance ?: synchronized(this) {
                val Instance = ProductDetailsRepository( remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getProductByID(id: String): Flow<ProductResponse> {
        return flowOf(productDetailsRemoteSource.getProductByID(id))
    }

    override suspend fun getDraftOrderById(id: String): Flow<DraftOrderResponse> {
        return flowOf(productDetailsRemoteSource.getDraftOrderByID(id))
    }

    override suspend fun modifyDraftOrder(
        id: String,
        draftOrder: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return flowOf(productDetailsRemoteSource.modifyDraftOrder(id,draftOrder))
    }
}