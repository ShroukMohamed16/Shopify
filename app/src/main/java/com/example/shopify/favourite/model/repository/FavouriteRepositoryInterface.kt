package com.example.shopify.favourite.model.repository

import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface FavouriteRepositoryInterface {

    suspend fun getFavDraftOrder(id:String): Flow<DraftOrderResponse>
    suspend fun modifyFavDraftOrder(id:String,orderResponse: DraftOrderResponse): Flow<DraftOrderResponse>
}