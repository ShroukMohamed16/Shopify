package com.example.shopify.homeFragment.Model.Repository

import android.util.Log
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodes
import com.example.shopify.homeFragment.Model.DataCalss.PriceRules
import com.example.shopify.homeFragment.Remote.BrandsRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BrandsRepository(var brandRemote :BrandsRemoteSource) : BrandsRepositoryInterface{

    companion object {
        private var instance: BrandsRepository? = null
        fun getInstance(remote: BrandsRemoteSource): BrandsRepository {
            return instance ?: synchronized(this) {
                val Instance = BrandsRepository( remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getBrands(): Flow<AllBrandsModel> {
        Log.i("TAG", "getBrands: repository")
        return flowOf(brandRemote.getBrands())
    }

    override suspend fun getDiscountCodes(id:Long): Flow<DiscountCodes> {
        return flowOf(brandRemote.getDiscountCodes(id))
    }

    override suspend fun getPriceRules(): Flow<PriceRules> {
        return flowOf(brandRemote.getPriceRules())
    }
}