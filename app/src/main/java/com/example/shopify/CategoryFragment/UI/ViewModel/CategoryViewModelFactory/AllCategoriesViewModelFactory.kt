package com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepositoryInterface
import com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModel.AllCategoriesViewModel

class AllCategoriesViewModelFactory (private val repo : AllCategoriesRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(AllCategoriesViewModel::class.java)){
            AllCategoriesViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}