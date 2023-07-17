package com.example.shopify.homeFragment.Remote

import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel

interface BrandsRemoteSource {
    suspend fun getBrands() : AllBrandsModel

}