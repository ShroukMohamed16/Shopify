package com.example.shopify.search.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.search.model.repository.SearchRepositoryInterface

class SearchViewModelFactory (private val repo : SearchRepositoryInterface) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
                SearchViewModel(repo) as T
            }else {
                throw java.lang.IllegalArgumentException("viewModel class not found")
            }
        }
}