package com.example.shopify.setting.Remote

import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyClient : CurrencyRemoteSource {
    private val currency_service: CurrencyServies

    init {
        val BASE_URL = "https://v6.exchangerate-api.com/v6/dd010a73be92b99acb505575/"
        val gson = GsonBuilder().create()
        val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        currency_service = retrofitInstance.create(CurrencyServies::class.java)
    }

    override suspend fun getAllCurrencies(): MutableStateFlow<CurrencyResponse?> {
        return MutableStateFlow(currency_service.getAllCurrencies())
    }

    override suspend fun changeCurrency(toCode: String): MutableStateFlow<ExchangeResponse?> {
        return MutableStateFlow(currency_service.changeCurrency(toCode))
    }

}