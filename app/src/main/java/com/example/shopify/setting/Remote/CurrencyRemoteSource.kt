package com.example.shopify.setting.Remote

import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import kotlinx.coroutines.flow.MutableStateFlow

interface CurrencyRemoteSource {
    suspend fun getAllCurrencies(): MutableStateFlow<CurrencyResponse?>
    suspend fun changeCurrency(toCode:String):MutableStateFlow<ExchangeResponse?>
}