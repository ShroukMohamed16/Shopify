package com.example.shopify.setting.Model.SettingRepository

import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import kotlinx.coroutines.flow.MutableStateFlow

interface SettingRepositoryInterface {
    suspend fun getAllCurrencies(): MutableStateFlow<CurrencyResponse?>
    suspend fun changeCurrency(toCode:String) : MutableStateFlow<ExchangeResponse?>
}