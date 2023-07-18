package com.example.shopify.SubCategoryFragment.UI.ViewModel.SubCategoryViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.SubCategoryFragment.Model.AllProduct
import com.example.shopify.SubCategoryFragment.Model.SubCategoryRepository.SubCategoryRepositoryInterface
import com.example.shopify.base.State
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SubCategoryViewModel(private val repo : SubCategoryRepositoryInterface) :ViewModel() {
    private var productBrandState: MutableStateFlow<State<AllProduct>> = MutableStateFlow(State.Loading)
    val productBrand: StateFlow<State<AllProduct>> = productBrandState

    private var productCategoryState: MutableStateFlow<State<AllProduct>> = MutableStateFlow(State.Loading)
    val productCategory: StateFlow<State<AllProduct>> = productCategoryState

    fun getProductOfBrands(vendor:String){
        viewModelScope.launch (Dispatchers.IO){
            repo.getProductsOfBrand(vendor)
                ?.catch { e->
                    productBrandState.value=State.Failure(e) }
                ?.collect{ data->
                    productBrandState.value=State.Success(data)
                }
        }
    }

    fun getProductOfCategory(collectioId:Long){
        viewModelScope.launch(Dispatchers.IO){
            repo.getProductOfCategory(collectioId)
                ?.catch { e->
                    Log.e("TAG", "category product : $e", )
                    Log.i("TAG", "getProductOfCategory: faaaaaail")
                    productCategoryState.value=State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "category product : $data")
                    productCategoryState.value=State.Success(data)
                }
        }
    }


}