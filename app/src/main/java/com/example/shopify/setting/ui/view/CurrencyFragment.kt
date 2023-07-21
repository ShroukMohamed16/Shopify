package com.example.shopify.setting.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.setting.Model.SettingRepository.SettingRepository
import com.example.shopify.setting.Remote.CurrencyClient
import com.example.shopify.setting.ui.viewModel.SettingViewModel
import com.example.shopify.setting.ui.viewModel.SettingViewModelFactory
import kotlinx.coroutines.launch

class CurrencyFragment : DialogFragment() {
    lateinit var viewModel: SettingViewModel
    lateinit var factory: SettingViewModelFactory
    private var currencyList:List< List<String>> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = SettingViewModelFactory(SettingRepository.getInstance(CurrencyClient()))
        viewModel = ViewModelProvider(this, factory).get(SettingViewModel::class.java)

        viewModel.getAllCurrencies()
        lifecycleScope.launch {
            viewModel.currency.collect { result ->
                when (result) {
                    is State.Loading -> {

                    }
                    is State.Success -> {
                       currencyList = result.data.supported_codes
                        Log.i("TAG", "onViewCreated is currency setting : $currencyList")


                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: failur")


                    }
                }

            }
        }
    }
}





