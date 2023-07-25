package com.example.shopify.Payment.UI.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.Payment.Model.Repository.PaymentRepositoryInterface
import com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel.HomeViewModel

class PaymentViewModelFactory(private val repo :PaymentRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(PaymentViewModel::class.java)){
            PaymentViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}