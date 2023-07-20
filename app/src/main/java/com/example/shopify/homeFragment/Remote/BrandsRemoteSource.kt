package com.example.shopify.homeFragment.Remote

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodeModel

interface BrandsRemoteSource {
    suspend fun getBrands() : AllBrandsModel
    suspend fun getDiscountCodes() : DiscountCodeModel

}