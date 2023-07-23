package com.example.shopify.AllOrdersFragment.Remote

import android.util.Log
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.authentication.remote.AuthenticationService
import com.example.shopify.base.Remote.RetrofitHelper

class AllOrderClinet : AllOrderRemoteSource {

    val order_service: AllOrderService by lazy {
        RetrofitHelper.retrofitInstance.create(AllOrderService::class.java)
    }

    override suspend fun getAllOrdersOfUser(email: String): OrderResponse {
        Log.i("TAG", "getAllOrdersOfUser: client")
        return order_service.getAllOrdersOfUser(email)
    }


}