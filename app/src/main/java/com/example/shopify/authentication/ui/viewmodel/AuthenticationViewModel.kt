package com.example.shopify.authentication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.utilities.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private const val TAG = "AuthenticationViewModel"
class AuthenticationViewModel(val repositoryInterface: AuthenticationRepositoryInterface):ViewModel(){

    private var customerInfo: MutableStateFlow<State<CustomerResponse>> = MutableStateFlow(State.Loading)
    val customerDetails: StateFlow<State<CustomerResponse>> = customerInfo

    private var customerByEmail: MutableStateFlow<State<CustomerListResponse>> = MutableStateFlow(State.Loading)
    val customer: StateFlow<State<CustomerListResponse>> = customerByEmail

    private var draftOrder: MutableStateFlow<State<DraftOrderResponse>> = MutableStateFlow(State.Loading)
    val draft: StateFlow<State<DraftOrderResponse>> = draftOrder


    fun addCustomer(customer: CustomerResponse){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.addNewCustomerToAPI(customer)
                ?.catch { e->
                    Log.e("TAG", "getCustomer: $e", )
                    customerInfo.value= State.Failure(e)
                }
                ?.collect{ data->
                    Log.i("TAG", "getCustomer: $data")
                    customerInfo.value= State.Success(data)
                    Log.i(TAG, "addCustomer: ${data.customer.toString()}")
                }
        }
    }

    fun getCustomerByEmail(email: String){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.getCustomerByEmailFromAPI(email)
                ?.catch { e->
                    Log.e("TAG", "getCustomer: $e", )
                    customerByEmail.value= State.Failure(e)
                }
                ?.collect{ data->
                    Log.i("TAG", "getCustomer: $data")
                    customerByEmail.value= State.Success(data)
                    Constants.CustomerListResponseSize = data.getCustomers()?.size!!
                    Log.i(TAG, "getCustomerByEmail: ${Constants.CustomerListResponseSize.toString()}")
                    Log.i(TAG, "addCustomer: ${data.getCustomers()?.size}")
                }
        }
    }
}