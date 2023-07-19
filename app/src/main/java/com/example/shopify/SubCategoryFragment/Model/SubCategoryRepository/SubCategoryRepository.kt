package com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository

import com.example.shopify.SubCategoryFragment.Model.AllProduct
import com.example.shopify.SubCategoryFragment.Remote.ProductBrandRemoteSource
import com.example.shopify.homeFragment.Model.Repository.BrandsRepository
import com.example.shopify.homeFragment.Model.Repository.BrandsRepositoryInterface
import com.example.shopify.homeFragment.Remote.BrandsRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class SubCategoryRepository(var productRemote : ProductBrandRemoteSource) : SubCategoryRepositoryInterface {

    companion object {
        private var instance: SubCategoryRepository? = null
        fun getInstance(remote: ProductBrandRemoteSource): SubCategoryRepository {
            return instance ?: synchronized(this) {
                val Instance = SubCategoryRepository( remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getProductsOfBrand(vendor: String): Flow<AllProduct> {
       return flowOf(productRemote.getProductsBrand(vendor))
    }

    override suspend fun getProductOfCategory(
        productType: String,
        collectioId: Long,
    ): Flow<AllProduct> {
        return flowOf ( productRemote.getProductOfCategory( productType,collectioId) )
    }

    override suspend fun getAllProductOfCategory(collectioId: Long): Flow<AllProduct> {
        return flowOf(productRemote.getAllProductOfCategory(collectioId))
    }


}