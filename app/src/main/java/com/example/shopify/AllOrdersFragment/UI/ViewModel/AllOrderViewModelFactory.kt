package com.example.shopify.AllOrdersFragment.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepositoryInterface
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModel

class AllOrderViewModelFactory (val repository: AllOrderRepositoryInterface):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ALlOrderViewModel::class.java)){
            ALlOrderViewModel(repository) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }

}