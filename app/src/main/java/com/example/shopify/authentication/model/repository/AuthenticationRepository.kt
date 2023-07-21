package com.example.shopify.authentication.model.repository

import com.example.shopify.authentication.model.pojo.Customer
import com.example.shopify.authentication.remote.AuthenticationRemoteSource
import com.example.shopify.productinfo.model.repository.ProductDetailsRepository
import com.example.shopify.productinfo.remote.ProductDetailsRemoteSource

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

    override suspend fun addNewCustomerToAPI(customer: Customer) {
        remoteSource.addNewCustomer(customer)
    }

}