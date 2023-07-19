package com.example.shopify.SubCategoryFragment.Remote

import com.example.shopify.SubCategoryFragment.Model.AllProduct

interface ProductBrandRemoteSource {
    suspend fun getProductsBrand(vendor:String): AllProduct
    suspend fun getProductOfCategory( productType: String,collectioId:Long) : AllProduct
    suspend fun getAllProductOfCategory(collectioId:Long) : AllProduct
}