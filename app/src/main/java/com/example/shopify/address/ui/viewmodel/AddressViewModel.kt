package com.example.shopify.address.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import com.example.shopify.address.model.repository.AddressRepositoryInterface
import com.example.shopify.base.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AddressViewModel(private val repo: AddressRepositoryInterface) : ViewModel() {

    private var addressState: MutableStateFlow<State<AddressResponse?>> =
        MutableStateFlow(State.Loading)
    val address: StateFlow<State<AddressResponse?>> = addressState

    private var getAddressState: MutableStateFlow<State<GetAddressResponse?>> =
        MutableStateFlow(State.Loading)
    val getAddress: StateFlow<State<GetAddressResponse?>> = getAddressState

    fun getCustomerAddress(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getCustomerAddress(id)
                ?.catch { e ->
                    Log.e("TAG", "address: $e")
                    getAddressState.value = State.Failure(e)
                }
                ?.collect { data ->
                    Log.i("TAG", "addressData: $data")
                    getAddressState.value = State.Success(data)
                }
        }
    }

    fun addCustomerAddress(id: Long, address: AddressBody) {
        viewModelScope.launch {
            repo.addCustomerAddress(id, address)?.catch { e ->
                Log.e("TAG", "address: $e")
                addressState.value = State.Failure(e)
            }
                ?.collect { data ->
                    Log.i("TAG", "address: $data")
                    addressState.value = State.Success(data)
                }
        }
    }

    fun deleteCustomerAddress(customer_id: Long, address_id: Long) {
        viewModelScope.launch {
            repo.deleteCustomerAddress(customer_id, address_id)
            getCustomerAddress(customer_id)

        }
    }


}
