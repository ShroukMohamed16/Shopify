package com.example.shopify.authentication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.productinfo.ui.viewmodel.ProductsDetailsViewModel

class AuthenticationViewModelFactory(val repository: AuthenticationRepositoryInterface):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
            AuthenticationViewModel(repository) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }

}