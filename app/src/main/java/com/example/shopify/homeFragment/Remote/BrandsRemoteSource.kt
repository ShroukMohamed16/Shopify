package com.example.shopify.homeFragment.Remote

import com.example.shopify.homeFragment.Model.DataCalss.*

interface BrandsRemoteSource {
    suspend fun getBrands() : AllBrandsModel
    suspend fun getDiscountCodes(id:Long) : DiscountCodes
    suspend fun getPriceRules() : PriceRules
}