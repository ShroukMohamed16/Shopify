package com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository

import com.example.shopify.SubCategoryFragment.Model.AllProduct
import kotlinx.coroutines.flow.Flow

interface SubCategoryRepositoryInterface {
    suspend fun getProductsOfBrand(vendor : String) : Flow<AllProduct>
    suspend fun getProductOfCategory (productType: String,collectioId:Long) :Flow<AllProduct>
    suspend fun getAllProductOfCategory (collectioId:Long) :Flow<AllProduct>
}