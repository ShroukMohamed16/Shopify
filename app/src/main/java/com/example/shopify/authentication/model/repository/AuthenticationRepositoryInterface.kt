package com.example.shopify.authentication.model.repository

import com.example.shopify.authentication.model.pojo.CustomerResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepositoryInterface {

    suspend fun addNewCustomerToAPI(customer: CustomerResponse): Flow<CustomerResponse>
}