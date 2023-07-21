package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.Customer

interface AuthenticationRemoteSource {

    suspend fun addNewCustomer(customer: Customer)
}