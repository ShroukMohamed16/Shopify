package com.example.shopify.address.remote

import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path


interface AddressRemoteSource {

    suspend fun getCustomerAddress( id: Long): Flow<AddressResponse>

    suspend fun addCustomerAddress( id: String, address: AddressBody):AddressResponse

}