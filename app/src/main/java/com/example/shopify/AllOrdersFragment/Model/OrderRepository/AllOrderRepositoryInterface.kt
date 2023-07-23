package com.example.shopify.AllOrdersFragment.Model.OrderRepository

import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import kotlinx.coroutines.flow.Flow

interface AllOrderRepositoryInterface {
    suspend fun getAllOrdersOfUser(email:String) : Flow<OrderResponse>
}