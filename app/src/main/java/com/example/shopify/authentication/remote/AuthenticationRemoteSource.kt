package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body

interface AuthenticationRemoteSource {

    suspend fun addNewCustomer(customer: CustomerResponse): CustomerResponse
    suspend fun getCustomerByEmail(email:String): CustomerListResponse

    suspend fun deleteCustomer(customerID:Long)

}