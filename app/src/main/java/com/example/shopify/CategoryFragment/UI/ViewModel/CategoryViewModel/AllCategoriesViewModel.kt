package com.example.shopify.CategoryFragment.UI.ViewModel.CategoryViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories
import com.example.shopify.CategoryFragment.Model.Repository.AllCategoriesRepositoryInterface
import com.example.shopify.base.State
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllCategoriesViewModel(private val repo : AllCategoriesRepositoryInterface) :ViewModel() {

    private var categoryState: MutableStateFlow<State<Allcategories>> = MutableStateFlow(State.Loading)
    val category: StateFlow<State<Allcategories>> = categoryState

    fun getAllCategories(){
        Log.i("TAG", "onViewCreated: categ view model start of scoope")
        viewModelScope.launch(Dispatchers.IO) {
            repo.getAllCategories()
                ?.catch { e->
                    Log.e("TAG", "category: $e", )
                    categoryState.value=State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "category: $data")
                    categoryState.value=State.Success(data)
                }
        }
        Log.i("TAG", "onViewCreated: categ view model end of scoope")
    }
}