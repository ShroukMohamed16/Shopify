package com.example.shopify.homeFragment.Remote

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodeModel
import retrofit2.http.GET
import retrofit2.http.Headers

interface BrandsService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("smart_collections.json")
    suspend fun getBrands() : AllBrandsModel

    @Headers("X-Shopify-Access-Token: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")
    @GET("price_rules/1389437387026/discount_codes.json")
    suspend fun getDiscountCodes() : DiscountCodeModel
}
