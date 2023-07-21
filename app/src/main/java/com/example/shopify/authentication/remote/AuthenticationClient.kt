package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.Customer
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.productinfo.remote.ProductDetailsService

class AuthenticationClient:AuthenticationRemoteSource {

    val authenticationService: AuthenticationService by lazy {
        RetrofitHelper.retrofitInstance.create(AuthenticationService::class.java)
    }


    override suspend fun addNewCustomer(customer: Customer) {
        authenticationService.addNewCustomer(customer)
    }
}