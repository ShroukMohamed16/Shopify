package com.example.shopify.address.model.repository

import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepository
import com.example.shopify.CategoryFragment.Remote.AllCategoriesRemoteSource
import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.remote.AddressRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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
    override suspend fun getCustomerAddress(id: Long): Flow<AddressResponse> {
        return remoteAddress.getCustomerAddress(id)
    }

    override
    suspend fun addCustomerAddress(@Path("id") id: String, address: Address):Flow<AddressResponse >{
      return flowOf( remoteAddress.addCustomerAddress(id,address))
    }
}