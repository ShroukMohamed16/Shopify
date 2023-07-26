package com.example.shopify.CartFragment.UI.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.CartFragment.model.CartRepositoryInterface
import com.example.shopify.address.model.repository.AddressRepositoryInterface
import com.example.shopify.address.ui.viewmodel.AddressViewModel

class CartViewModelFactory(val repository: CartRepositoryInterface):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(CartViewModel::class.java)){
            CartViewModel(repository) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }

}