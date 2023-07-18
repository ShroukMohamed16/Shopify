package com.example.shopify.CategoryFragment.Remote

import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories
import retrofit2.http.GET
import retrofit2.http.Headers

interface AllCategoriesService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("custom_collections.json")
    suspend fun getAllCategories() : Allcategories
}