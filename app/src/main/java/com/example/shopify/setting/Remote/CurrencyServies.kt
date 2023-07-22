package com.example.shopify.setting.Remote

import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyServies {
    @GET("codes")
    suspend fun getAllCurrencies():CurrencyResponse

    @GET("pair/EGP/{toCode}")
    suspend fun changeCurrency(@Path(value = "toCode")toCode:String): ExchangeResponse
}