package com.example.shopify.authentication.model.repository

import com.example.shopify.authentication.model.pojo.Customer

interface AuthenticationRepositoryInterface {

    suspend fun addNewCustomerToAPI(customer: Customer)
}