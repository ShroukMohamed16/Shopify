package com.example.shopify.SubCategoryFragment.Remote

import com.example.shopify.SubCategoryFragment.Model.AllProduct

interface ProductBrandRemoteSource {
    suspend fun getProductsBrand(vendor:String): AllProduct
    suspend fun getProductOfCategory( collectioId:Long) : AllProduct
}