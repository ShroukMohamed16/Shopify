package com.example.shopify.favourite.model.repository

import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow

interface FavouriteRepositoryInterface {

    suspend fun getFavDraftOrder(id:Long): Flow<DraftOrderResponse>
    suspend fun modifyFavDraftOrder(id:Long,orderResponse: DraftOrderResponse): Flow<DraftOrderResponse>
}