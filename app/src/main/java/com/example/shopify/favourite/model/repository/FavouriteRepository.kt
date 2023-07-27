package com.example.shopify.favourite.model.repository

import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.favourite.remote.FavouriteRemoteSource
import kotlinx.coroutines.flow.Flow


class FavouriteRepository(private val favouriteRemoteSource: FavouriteRemoteSource):
    FavouriteRepositoryInterface {

    companion object {
        private var instance: FavouriteRepository? = null
        fun getInstance(remote: FavouriteRemoteSource): FavouriteRepository {
            return instance ?: synchronized(this) {
                val Instance = FavouriteRepository(remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getFavDraftOrder(id: String): Flow<DraftOrderResponse> {
        return favouriteRemoteSource.getFavDraftOrderById(id)
    }

    override suspend fun modifyFavDraftOrder(
        id: String,
        orderResponse: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return favouriteRemoteSource.modifyFavDraftOrder(id,orderResponse)
    }


}