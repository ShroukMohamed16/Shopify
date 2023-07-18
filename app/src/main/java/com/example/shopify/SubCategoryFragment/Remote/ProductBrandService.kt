package com.example.shopify.SubCategoryFragment.Remote

import com.example.shopify.SubCategoryFragment.Model.AllProduct
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ProductBrandService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("products.json")
    suspend fun getProductsOfBrand(@Query("vendor") vendor: String): AllProduct

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("products.json")
    suspend fun getProductsOfCategory(
        @Query("product_type") productType: String,
        @Query("collection_id") collectionId: Long
    ): AllProduct
}