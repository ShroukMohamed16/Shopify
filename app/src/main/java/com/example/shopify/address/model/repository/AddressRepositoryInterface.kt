package com.example.shopify.address.model.repository

import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface AddressRepositoryInterface {
    suspend fun getCustomerAddress( id: Long): Flow<GetAddressResponse?>
    suspend fun addCustomerAddress(@Path("id") id: Long, address: AddressBody):Flow<AddressResponse?>
    suspend fun deleteCustomerAddress( customer_id:Long , address_id:Long)

}