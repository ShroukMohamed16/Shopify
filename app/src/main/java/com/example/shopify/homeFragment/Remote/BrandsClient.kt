package com.example.shopify.homeFragment.Remote

import android.util.Log
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodes
import com.example.shopify.homeFragment.Model.DataCalss.PriceRules

class BrandsClient : BrandsRemoteSource {

    val brandService: BrandsService  by lazy {
        RetrofitHelper.retrofitInstance.create(BrandsService::class.java)
    }

    override suspend fun getBrands(): AllBrandsModel {
        Log.i("TAG", "getBrands: client")
        return brandService.getBrands()
    }

    override suspend fun getDiscountCodes(id:Long): DiscountCodes {
        return brandService.getDiscountCodes(id)
    }

    override suspend fun getPriceRules(): PriceRules {
        return brandService.getPriceRules()
    }
}