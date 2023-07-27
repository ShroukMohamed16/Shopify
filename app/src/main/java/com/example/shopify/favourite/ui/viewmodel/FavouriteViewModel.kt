package com.example.shopify.favourite.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.favourite.model.repository.FavouriteRepositoryInterface
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouriteViewModel(var favouriteRepositoryInterface: FavouriteRepositoryInterface):ViewModel(){

    private var draftOrder: MutableStateFlow<State<DraftOrderResponse>> = MutableStateFlow(State.Loading)
    val draftOrderDetails: StateFlow<State<DraftOrderResponse>> = draftOrder

    private var draftOrderList: MutableStateFlow<State<DraftOrderResponse>> = MutableStateFlow(State.Loading)
    val draftOrderDetailsList: StateFlow<State<DraftOrderResponse>> = draftOrderList


     fun getFavDraftOrder(id:String){
         viewModelScope.launch(Dispatchers.IO){
             favouriteRepositoryInterface.getFavDraftOrder(id)
                 ?.catch { e ->
                     draftOrderList.value = State.Failure(e)
                 }
                 ?.collect { data ->
                     draftOrderList.value = State.Success(data)
                 }
         }
    }

    fun modifyFavDraftOrder(id:String,draftOrderResponse: DraftOrderResponse){
        viewModelScope.launch(Dispatchers.IO){
            favouriteRepositoryInterface.modifyFavDraftOrder(id,draftOrderResponse)
                ?.catch { e ->
                    draftOrder.value = State.Failure(e)
                }
                ?.collect { data ->
                    draftOrder.value = State.Success(data)
                }
        }
    }

}