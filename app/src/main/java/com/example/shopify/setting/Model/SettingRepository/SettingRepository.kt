package com.example.shopify.setting.Model.SettingRepository

import com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository.SubCategoryRepository
import com.example.shopify.SubCategoryFragment.Remote.ProductBrandRemoteSource
import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import com.example.shopify.setting.Remote.CurrencyRemoteSource
import kotlinx.coroutines.flow.MutableStateFlow

class SettingRepository(private val currencyRemoteSource:CurrencyRemoteSource) : SettingRepositoryInterface {

    companion object {
        private var instance: SettingRepository? = null
        fun getInstance(remote: CurrencyRemoteSource): SettingRepository {
            return instance ?: synchronized(this) {
                val Instance = SettingRepository( remote)
                instance = Instance
                Instance
            }
        }
    }


    override suspend fun getAllCurrencies(): MutableStateFlow<CurrencyResponse?> {
        return currencyRemoteSource.getAllCurrencies()
    }

    override suspend fun changeCurrency(toCode: String): MutableStateFlow<ExchangeResponse?> {
        return currencyRemoteSource.changeCurrency(toCode)
    }
}