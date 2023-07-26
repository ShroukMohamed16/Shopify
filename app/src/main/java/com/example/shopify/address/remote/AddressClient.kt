package com.example.shopify.address.remote

import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.base.Remote.RetrofitHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.http.Path

class AddressClient private constructor():AddressRemoteSource {
    val addressService: AddressService by lazy {
        RetrofitHelper.retrofitInstance.create(AddressService::class.java)
    }
    companion object {
        private var instance: AddressClient? = null
        fun getInstance(): AddressClient {
            return instance ?: synchronized(this) {
                val temp = AddressClient()
                instance = temp
                temp
            }
        }
    }
    override suspend fun getCustomerAddress(id: Long): Flow<GetAddressResponse?> {
        return flowOf(addressService.getCustomerAddress(id))
    }

    override suspend fun addCustomerAddress(@Path("id") id: Long, address: AddressBody): Flow<AddressResponse?> {
        return flowOf(addressService.addCustomerAddress(id,address).body())
    }

    override suspend fun deleteCustomerAddress(
        customer_id: Long,
        address_id: Long
    ){
         flowOf(addressService.deleteCustomerAddress(customer_id,address_id))
    }
}