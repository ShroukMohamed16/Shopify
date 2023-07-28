package com.example.shopify.favourite.remote

import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface FavouriteRemoteSource {
    suspend fun getFavDraftOrderById(id:Long) : Flow<DraftOrderResponse>
    suspend fun modifyFavDraftOrder(id: Long,orderResponse: DraftOrderResponse):Flow<DraftOrderResponse>
}