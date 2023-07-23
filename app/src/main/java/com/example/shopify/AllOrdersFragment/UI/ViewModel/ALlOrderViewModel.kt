package com.example.shopify.AllOrdersFragment.UI.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepositoryInterface
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.base.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ALlOrderViewModel(val repo: AllOrderRepositoryInterface) : ViewModel() {

    private var orderState: MutableStateFlow<State<OrderResponse>> = MutableStateFlow(State.Loading)
    val order: StateFlow<State<OrderResponse>> = orderState

    fun getAllOrdersOfUser(email: String) {
        viewModelScope.launch {
            Log.i("TAG", "getAllOrdersOfUser: start of view model")
            repo.getAllOrdersOfUser(email)
                ?.catch { e ->
                    Log.i("TAG", "getAllOrdersOfUser: view model catch")
                    orderState.value = State.Failure(e)
                }
                ?.collect { data ->
                    Log.i("TAG", "getAllOrdersOfUser: view model success")
                    Log.i("TAG", "getCustomer: $data")
                    orderState.value = State.Success(data)
                }
        }
    }
}