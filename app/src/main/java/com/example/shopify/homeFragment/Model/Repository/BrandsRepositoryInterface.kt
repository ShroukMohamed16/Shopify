package com.example.shopify.homeFragment.Model.Repository

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodeModel
import kotlinx.coroutines.flow.Flow

interface BrandsRepositoryInterface {
    suspend fun getBrands(): Flow<AllBrandsModel>
    suspend fun getDiscountCodes() : Flow<DiscountCodeModel>
}