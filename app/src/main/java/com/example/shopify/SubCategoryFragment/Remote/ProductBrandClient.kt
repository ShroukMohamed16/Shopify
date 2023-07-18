package com.example.shopify.SubCategoryFragment.Remote

import com.example.shopify.SubCategoryFragment.Model.AllProduct
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.homeFragment.Remote.BrandsService

class ProductBrandClient : ProductBrandRemoteSource {
    val brandService: ProductBrandService by lazy {
        RetrofitHelper.retrofitInstance.create(ProductBrandService::class.java)
    }

    override suspend fun getProductsBrand(vendor: String): AllProduct {
        return brandService.getProductsOfBrand(vendor)
    }

    override suspend fun getProductOfCategory( productType: String,collectioId: Long) : AllProduct{
        return brandService.getProductsOfCategory(productType,collectioId)
    }
}