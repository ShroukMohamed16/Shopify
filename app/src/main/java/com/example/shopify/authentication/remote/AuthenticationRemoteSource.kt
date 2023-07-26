package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.*
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteSource {

    suspend fun addNewCustomer(customer: CustomerResponse): CustomerResponse
    suspend fun getCustomerByEmail(email:String): CustomerListResponse
    suspend fun createDraftOrder(orderResponse: DraftOrderResponse):DraftOrderResponse
    suspend fun deleteCustomer(customerID:Long)

    suspend fun updateCustomer(
        customer_id: Long,
        customer: CustomerResponsePut
    ): CustomerResponsePut
}