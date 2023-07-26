package com.example.shopify.address.remote

import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Path


interface AddressRemoteSource {

    suspend fun getCustomerAddress( id: Long): Flow<GetAddressResponse?>

    suspend fun addCustomerAddress(@Path("id") id: Long, address: AddressBody):Flow<AddressResponse?>
    suspend fun deleteCustomerAddress( customer_id:Long , address_id:Long)

}