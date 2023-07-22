package com.example.shopify.setting.Model

data class CurrencyResponse(    val documentation: String,
                                val result: String,
                                val supported_codes: List<List<String>>,
                                val terms_of_use: String)
