package com.example.shopify.homeFragment.Model.Repository

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodes
import com.example.shopify.homeFragment.Model.DataCalss.PriceRules
import kotlinx.coroutines.flow.Flow

interface BrandsRepositoryInterface {
    suspend fun getBrands(): Flow<AllBrandsModel>
    suspend fun getDiscountCodes(id:Long) : Flow<DiscountCodes>
    suspend fun getPriceRules() : Flow<PriceRules>
}