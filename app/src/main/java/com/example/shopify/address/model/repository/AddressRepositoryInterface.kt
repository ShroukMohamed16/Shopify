package com.example.shopify.address.model.repository

import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface AddressRepositoryInterface {
    suspend fun getCustomerAddress( id: Long): Flow<AddressResponse>

    suspend fun addCustomerAddress(@Path("id") id: String, address: AddressBody):Flow<AddressResponse>
}