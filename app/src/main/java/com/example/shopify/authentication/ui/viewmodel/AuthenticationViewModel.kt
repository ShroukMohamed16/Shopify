package com.example.shopify.authentication.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.authentication.model.pojo.CustomerBodey
import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.pojo.Customerbody
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.utilities.Constants
import com.example.shopify.utilities.MySharedPreferences
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

    private var draftOrderState: MutableStateFlow<State<DraftOrderResponse>> = MutableStateFlow(State.Loading)
    val draftOrder: StateFlow<State<DraftOrderResponse>> = draftOrderState


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

    fun getCustomerByEmail(email: String,context: Context){
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
                    if(Constants.CustomerListResponseSize != 0 ) {
                        MySharedPreferences.getInstance(context)
                            .saveCustomerID(data.getCustomers()!![0]?.id!!)
                    }
                    Log.i(TAG, "getCustomerByEmail: ${Constants.CustomerListResponseSize.toString()}")
                    Log.i(TAG, "addCustomer: ${data.getCustomers()?.size}")
                }
        }
    }
    fun createDraftOrder(draftOrderResponse: DraftOrderResponse){
        viewModelScope.launch {
            repositoryInterface.addDraftOrder(draftOrderResponse)
                ?.catch { e->
                    draftOrderState.value=State.Failure(e)
                }
                ?.collect{ data->
                    Log.i("TAG", "getDraftOrder: $data")
                    draftOrderState.value=State.Success(data)
                }
        }

    }
    fun updateCustomer(customer_id:Long,customer: Customerbody){
        viewModelScope.launch{
         repositoryInterface.updateCustomer(customer_id,customer)
        }
    }
}