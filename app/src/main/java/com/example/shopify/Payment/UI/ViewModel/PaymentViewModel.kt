package com.example.shopify.Payment.UI.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.Payment.Model.DataClass.PostOrder
import com.example.shopify.Payment.Model.Repository.PaymentRepositoryInterface
import com.example.shopify.base.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PaymentViewModel(private val repo: PaymentRepositoryInterface) : ViewModel() {
    private var orderState: MutableStateFlow<State<Boolean>> = MutableStateFlow(State.Loading)
    val order: StateFlow<State<Boolean>> = orderState


    fun postOrder(id : Long) {
        viewModelScope.launch {
            repo.postOrder(id)
                ?.catch { e->
                    Log.e("TAG", "getAllBrands: $e", )
                    orderState.value=State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "getAllBrands: $data")
                    orderState.value=State.Success(true)
                }
        }
    }
}