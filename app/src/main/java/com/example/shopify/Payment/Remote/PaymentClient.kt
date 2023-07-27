package com.example.shopify.Payment.Remote

import android.util.Log
import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderReq
import com.example.shopify.AllOrdersFragment.Model.OrderResponse
import com.example.shopify.App
import com.example.shopify.Payment.Model.DataClass.PostOrder
import com.example.shopify.authentication.remote.AuthenticationService
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.base.line_items
import com.example.shopify.utilities.MySharedPreferences

class PaymentClient : PaymentRemoteSource {
    val payment_Service: PaymentService by lazy {
        RetrofitHelper.retrofitInstance.create(PaymentService::class.java)
    }
    val Auth_Service: AuthenticationService by lazy {
        RetrofitHelper.retrofitInstance.create(AuthenticationService::class.java)
    }

    override suspend fun postOrder(id: Long): Boolean {
        var newDraftOrderId: Long? = 0L
        var orderSuccess = false
        try {
            payment_Service.createOrder(id)
            val newDraftOrderResponse = Auth_Service.createDraftOrder(DraftOrderResponse(
                MySharedPreferences.getInstance(App.appContext).getCustomerEmail()?.let {
                    DraftOrder(email = it,
                        name = "cart_draft",
                        line_items = listOf(
                            line_items(
                                title = "empty",
                                quantity = 1,
                                price = "0"
                            )
                        ))
                }))

            newDraftOrderId = newDraftOrderResponse.draft_order?.id

            MySharedPreferences.getInstance(App.appContext).saveCartID(newDraftOrderId!!)
            orderSuccess = true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", e.message.toString())
            orderSuccess = false

        }
        return orderSuccess
    }

}