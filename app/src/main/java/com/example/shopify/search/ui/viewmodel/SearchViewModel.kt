package com.example.shopify.search.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.base.State
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.search.model.pojo.ProductListResponse
import com.example.shopify.search.model.repository.SearchRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(private var searchRepositoryInterface: SearchRepositoryInterface):ViewModel(){
    private var productList: MutableStateFlow<State<ProductListResponse>> = MutableStateFlow(State.Loading)
    val products: StateFlow<State<ProductListResponse>> = productList

    fun getProducts() {
        Log.i("TAG", "start to get Product Info")
        viewModelScope.launch(Dispatchers.IO) {
            searchRepositoryInterface.getProductsFromAPI()
                ?.catch { e->
                    productList.value= State.Failure(e) }
                ?.collect{ data->
                    productList.value= State.Success(data)
                }

        }
        Log.i("TAG", "Finish Product Info")
    }


}