package com.example.shopify.homeFragment.Remote

import com.example.shopify.homeFragment.Model.DataCalss.*
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface BrandsService {
    @Headers(
        "Content-Type:application/json",
        "X-Shopify-Access-Token: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"
    )
    @GET("smart_collections.json")
    suspend fun getBrands(): AllBrandsModel

    /*
        @Headers("X-Shopify-Access-Token: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")
        @GET("price_rules/1389436895506/discount_codes.json")
        suspend fun getDiscountCodes() : DiscountCodes*/

    @Headers("X-Shopify-Access-Token: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")
    @GET("price_rules/{id}/discount_codes.json")
    suspend fun getDiscountCodes(@Path("id") id: Long): DiscountCodes

    @Headers("X-Shopify-Access-Token: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")
    @GET("price_rules.json")
    suspend fun getPriceRules(): PriceRules
}
