package com.example.shopify.productinfo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel
import com.example.shopify.productinfo.model.repository.ProductDetailsRepositoryInterface

class ProductsDetailsViewModelFactory(private val repo : ProductDetailsRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ProductsDetailsViewModel::class.java)){
            ProductsDetailsViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}