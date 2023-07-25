package com.example.shopify.address.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.address.model.repository.AddressRepositoryInterface
import com.example.shopify.authentication.model.repository.AuthenticationRepositoryInterface
import com.example.shopify.authentication.ui.viewmodel.AuthenticationViewModel


class AddressViewModelFactory(val repository: AddressRepositoryInterface):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AddressViewModel::class.java)){
            AddressViewModel(repository) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }

}