package com.example.shopify.setting.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.setting.Model.SettingRepository.SettingRepositoryInterface

class SettingViewModelFactory (private val repo : SettingRepositoryInterface) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(SettingViewModel::class.java)){
            SettingViewModel(repo) as T
        }else {
            throw java.lang.IllegalArgumentException("viewModel class not found")
        }
    }
}