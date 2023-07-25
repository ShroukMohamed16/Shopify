package com.example.shopify.authentication.model.repository

import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.remote.AuthenticationRemoteSource
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AuthenticationRepository(val remoteSource: AuthenticationRemoteSource):AuthenticationRepositoryInterface{

    companion object {
        private var instance: AuthenticationRepository? = null
        fun getInstance(remoteSource: AuthenticationRemoteSource): AuthenticationRepository {
            return instance ?: synchronized(this) {
                val Instance = AuthenticationRepository(remoteSource)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun addNewCustomerToAPI(customer: CustomerResponse): Flow<CustomerResponse> {
        return flowOf(remoteSource.addNewCustomer(customer))
    }

    override suspend fun getCustomerByEmailFromAPI(email: String): Flow<CustomerListResponse> {
        return flowOf(remoteSource.getCustomerByEmail(email))
    }



}