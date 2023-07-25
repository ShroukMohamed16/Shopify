package com.example.shopify.address.model.repository

import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.address.remote.AddressRemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

class AddressRepository(var remoteAddress : AddressRemoteSource):AddressRepositoryInterface {

    companion object {
        private var instance: AddressRepository? = null
        fun getInstance(remote: AddressRemoteSource): AddressRepository {
            return instance ?: synchronized(this) {
                val Instance = AddressRepository( remote)
                instance = Instance
                Instance
            }
        }
    }
    override suspend fun getCustomerAddress(id: Long): Flow<GetAddressResponse?> {
        return remoteAddress.getCustomerAddress(id)
    }

    override
    suspend fun addCustomerAddress(@Path("id") id: Long, address: AddressBody):Flow<AddressResponse?>{
      return remoteAddress.addCustomerAddress(id,address)
    }

    override suspend fun deleteCustomerAddress(
        customer_id: Long,
        address_id: Long
    ){
         remoteAddress.deleteCustomerAddress(customer_id,address_id)
    }
}