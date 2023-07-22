package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.base.Remote.RetrofitHelper
import kotlinx.coroutines.flow.Flow

class AuthenticationClient:AuthenticationRemoteSource {

    val authenticationService: AuthenticationService by lazy {
        RetrofitHelper.retrofitInstance.create(AuthenticationService::class.java)
    }


    override suspend fun addNewCustomer(customer: CustomerResponse): CustomerResponse {
        return authenticationService.addNewCustomer(customer)
    }

    override suspend fun deleteCustomer(customerID: Long) {
      authenticationService.deleteCustomer(customerID)
    }
}