package com.example.shopify.homeFragment.UI.ViewModel.HomeViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.homeFragment.Model.Repository.BrandsRepositoryInterface
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel

class HomeViewModelFactory (private val repo :BrandsRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}