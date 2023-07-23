package com.example.shopify.setting.ui.viewModel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.SubCategoryFragment.Model.AllProduct
import com.example.shopify.base.State
import com.example.shopify.setting.Model.CurrencyResponse
import com.example.shopify.setting.Model.ExchangeResponse
import com.example.shopify.setting.Model.SettingRepository.SettingRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SettingViewModel(private val repo :SettingRepositoryInterface) :ViewModel() {
    private var currencyState: MutableStateFlow<State<CurrencyResponse>> = MutableStateFlow(State.Loading)
    val currency: StateFlow<State<CurrencyResponse>> = currencyState

    private var changeCurrencyState: MutableStateFlow<State<ExchangeResponse>> = MutableStateFlow(State.Loading)
    val changeCurrency: StateFlow<State<ExchangeResponse>> = changeCurrencyState

    fun getAllCurrencies(){
        viewModelScope.launch (Dispatchers.IO){
            repo.getAllCurrencies()
                ?.catch { e->
                    currencyState.value=State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "category product : $data")
                    currencyState.value= State.Success(data!!)
                }

        }
    }
    fun changeCurrency(toCode :String){

        viewModelScope.launch (Dispatchers.IO){
            repo.changeCurrency(toCode)
                ?.catch { e->
                    changeCurrencyState.value=State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "category product : $data")
                    changeCurrencyState.value= State.Success(data!!)
                }

        }
    }

}