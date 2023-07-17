package com.example.shopify.homeFragment.Model.Repository

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import kotlinx.coroutines.flow.Flow

interface BrandsRepositoryInterface {
    suspend fun getBrands(): Flow<AllBrandsModel>
}