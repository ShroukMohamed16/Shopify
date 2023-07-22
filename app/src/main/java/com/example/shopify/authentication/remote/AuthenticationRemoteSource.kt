package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteSource {

    suspend fun addNewCustomer(customer: CustomerResponse): Flow<CustomerResponse>
}