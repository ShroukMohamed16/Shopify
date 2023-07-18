package com.example.shopify.homeFragment.UI.ViewModel.HomeViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.base.State
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.Repository.BrandsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repo :BrandsRepositoryInterface) :ViewModel() {

    private var brandState: MutableStateFlow<State<AllBrandsModel>> = MutableStateFlow(State.Loading)
    val brand: StateFlow<State<AllBrandsModel>> = brandState

        fun getAllBrands() {
            Log.i("TAG", "getAllBrands: view model")
            viewModelScope.launch(Dispatchers.IO) {
                repo.getBrands()
                    ?.catch { e->
                        Log.e("TAG", "getAllBrands: $e", )
                        brandState.value=State.Failure(e) }
                    ?.collect{ data->
                        Log.i("TAG", "getAllBrands: $data")
                        brandState.value=State.Success(data)
                    }


            }
            Log.i("TAG", "getAllBrands: out of scoope")
        }
}