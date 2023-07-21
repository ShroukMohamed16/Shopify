package com.example.shopify.base.Remote

import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val BASE_URL = "https://3e6b671cedf3fa95acc490daf89b9819:3daf7d84c3b600ac6fada82fef25f02e@itp-sv-and3.myshopify.com/admin/api/2023-07/"
object RetrofitHelper {
    val gson = GsonBuilder()
        .setLenient()
        .create()
    val retrofitInstance = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}