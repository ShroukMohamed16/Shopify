package com.example.shopify.favourite.remote

import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.productinfo.remote.ProductDetailsRemoteSource
import com.example.shopify.productinfo.remote.ProductDetailsService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FavouriteClient: FavouriteRemoteSource {

    val favouriteService: FavouriteService by lazy {
        RetrofitHelper.retrofitInstance.create(FavouriteService::class.java)
    }

    override suspend fun getFavDraftOrderById(id: String): Flow<DraftOrderResponse> {
        return flowOf(favouriteService.getFavDraftOrderById(id))
    }

    override suspend fun modifyFavDraftOrder(
        id: String,
        orderResponse: DraftOrderResponse
    ): Flow<DraftOrderResponse> {
        return flowOf(favouriteService.modifyFavDraftOrder(id,orderResponse))
    }


}