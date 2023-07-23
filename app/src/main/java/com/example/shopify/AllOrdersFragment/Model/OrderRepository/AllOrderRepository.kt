package com.example.shopify.AllOrdersFragment.Model.OrderRepository

import android.util.Log
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.AllOrdersFragment.Remote.AllOrderRemoteSource
import com.example.shopify.authentication.model.repository.AuthenticationRepository
import com.example.shopify.authentication.remote.AuthenticationRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AllOrderRepository(private var remoteSource : AllOrderRemoteSource) : AllOrderRepositoryInterface {
    companion object {
        private var instance: AllOrderRepository? = null
        fun getInstance(remoteSource: AllOrderRemoteSource): AllOrderRepository {
            return instance ?: synchronized(this) {
                val Instance = AllOrderRepository(remoteSource)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getAllOrdersOfUser(email: String): Flow<OrderResponse> {
        Log.i("TAG", "getAllOrdersOfUser: repo ")
        return flowOf(remoteSource.getAllOrdersOfUser(email))
    }

}