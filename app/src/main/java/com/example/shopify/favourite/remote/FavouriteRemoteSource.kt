package com.example.shopify.favourite.remote

import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Path

interface FavouriteRemoteSource {
    suspend fun getFavDraftOrderById(id:String) : Flow<DraftOrderResponse>
    suspend fun modifyFavDraftOrder(id: String,orderResponse: DraftOrderResponse):Flow<DraftOrderResponse>
}