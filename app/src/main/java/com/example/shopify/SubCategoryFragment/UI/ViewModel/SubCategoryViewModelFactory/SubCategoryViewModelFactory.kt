package com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository.SubCategoryRepositoryInterface
import com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModel.SubCategoryViewModel


class SubCategoryViewModelFactory (private val repo : SubCategoryRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SubCategoryViewModel::class.java)){
            SubCategoryViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}