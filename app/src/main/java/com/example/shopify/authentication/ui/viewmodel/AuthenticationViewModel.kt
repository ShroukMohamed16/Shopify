package com.example.shopify.authentication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.base.State
import com.example.shopify.productinfo.model.pojo.ProductResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AuthenticationViewModel(val repositoryInterface: AuthenticationRepositoryInterface):ViewModel(){

    private var customerInfo: MutableStateFlow<State<CustomerResponse>> = MutableStateFlow(State.Loading)
    val customer: StateFlow<State<CustomerResponse>> = customerInfo


    fun addCustomer(customer: CustomerResponse){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.addNewCustomerToAPI(customer)
                ?.catch { e->
                    Log.e("TAG", "getAllBrands: $e", )
                    customerInfo.value= State.Failure(e)
                }
                ?.collect{ data->
                    Log.i("TAG", "getAllBrands: $data")
                    customerInfo.value= State.Success(data)
                }
        }
    }
}