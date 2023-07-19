package com.example.shopify.homeFragment.Remote

import android.util.Log
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodeModel

class BrandsClient : BrandsRemoteSource {

    val brandService: BrandsService  by lazy {
        RetrofitHelper.retrofitInstance.create(BrandsService::class.java)
    }

    override suspend fun getBrands(): AllBrandsModel {
        Log.i("TAG", "getBrands: client")
        return brandService.getBrands()
    }

    override suspend fun getDiscountCodes(): DiscountCodeModel {
        return brandService.getDiscountCodes()
    }
}