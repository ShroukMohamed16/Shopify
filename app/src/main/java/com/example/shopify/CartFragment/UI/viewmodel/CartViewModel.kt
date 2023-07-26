package com.example.shopify.CartFragment.UI.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.CartFragment.model.CartRepositoryInterface
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CartViewModel(private val repo: CartRepositoryInterface) : ViewModel() {

    private var getCartState: MutableStateFlow<State<DraftOrderResponse>> =
        MutableStateFlow(State.Loading)
    val getCart: StateFlow<State<DraftOrderResponse>> = getCartState


    private var putCartState: MutableStateFlow<State<DraftOrderResponse>> =
        MutableStateFlow(State.Loading)
    val putCart: StateFlow<State<DraftOrderResponse>> = getCartState



    fun getCartDraftOrderById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCartDraftOrderById(id)
                ?.catch { e ->
                    Log.e("TAG", "cartFail: $e")
                    getCartState.value = State.Failure(e)
                }
                ?.collect { data ->
                    Log.i("TAG", "cartData: $data")
                    getCartState.value = State.Success(data)
                }
        }
    }

    fun editCartDraftOrderById(id: String,draftOrder: DraftOrderResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.editCartDraftOrderById(id,draftOrder)
                ?.catch { e ->
                    Log.e("TAG", "putCartFail: $e")
                    putCartState.value = State.Failure(e)
                }
                ?.collect { data ->
                    Log.i("TAG", "putCartData: $data")
                    putCartState.value = State.Success(data)
                }
        }
    }




}
