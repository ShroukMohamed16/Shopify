package com.example.shopify.productinfo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.State
import com.example.shopify.homeFragment.Model.DataCalss.AllBrandsModel
import com.example.shopify.homeFragment.Model.DataCalss.DiscountCodeModel
import com.example.shopify.productinfo.model.pojo.ProductResponse
import com.example.shopify.productinfo.model.repository.ProductDetailsRepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductsDetailsViewModel(private val repositoryInterface: ProductDetailsRepositoryInterface):ViewModel() {

    private var productInfo: MutableStateFlow<State<ProductResponse>> = MutableStateFlow(State.Loading)
    val product: StateFlow<State<ProductResponse>> = productInfo

    private var getDraftOrder: MutableStateFlow<State<DraftOrderResponse>> = MutableStateFlow(State.Loading)
    val getDraftOrderInfo: StateFlow<State<DraftOrderResponse>> = getDraftOrder


    fun getProductDetailsByID(id:String) {
        Log.i("TAG", "start to get Product Info")
        viewModelScope.launch(Dispatchers.IO) {
            repositoryInterface.getProductByID(id)
                ?.catch { e->
                    Log.e("TAG", "getProductInfo: $e", )
                    productInfo.value= State.Failure(e) }
                ?.collect{ data->
                    Log.i("TAG", "getProductInfo: $data")
                    productInfo.value= State.Success(data)
                }


        }
        Log.i("TAG", "Finish Product Info")
    }

    fun getDraftOrder(id: Long){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.getDraftOrderById(id)
                ?.catch { e ->
                    getDraftOrder.value = State.Failure(e)
                }
                ?.collect{ data ->
                    getDraftOrder.value = State.Success(data)
                }
        }
    }

    fun modifyDraftOrder(id: Long,order: DraftOrderResponse){
        viewModelScope.launch(Dispatchers.IO){
            repositoryInterface.modifyDraftOrder(id,order)
                ?.catch { e ->
                }
                ?.collect{ data ->
                }
        }
    }



}