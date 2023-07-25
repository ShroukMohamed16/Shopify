package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerBodey
import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.pojo.Customerbody
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface AuthenticationRemoteSource {

    suspend fun addNewCustomer(customer: CustomerResponse): CustomerResponse
    suspend fun getCustomerByEmail(email:String): CustomerListResponse
    suspend fun createDraftOrder(orderResponse: DraftOrderResponse):DraftOrderResponse
    suspend fun deleteCustomer(customerID:Long)

    suspend fun updateCustomer(
        customer_id: Long,
        customer: Customerbody
    ): CustomerBodey
}