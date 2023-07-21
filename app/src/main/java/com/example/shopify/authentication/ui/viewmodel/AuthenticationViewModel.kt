package com.example.shopify.authentication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.authentication.model.pojo.Customer
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthenticationViewModel(val repositoryInterface: AuthenticationRepositoryInterface):ViewModel(){

    fun addCustomer(customer: Customer){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.addNewCustomerToAPI(customer)
        }
    }
}