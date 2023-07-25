package com.example.shopify.Payment.UI.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.shopify.AllOrdersFragment.Model.*
import com.example.shopify.Payment.Model.DataClass.DraftOrdersItem
import com.example.shopify.Payment.Model.DataClass.LineItem
import com.example.shopify.Payment.Model.DataClass.PostOrder
import com.example.shopify.Payment.Model.Repository.PaymentRepository
import com.example.shopify.Payment.Remote.PaymentClient
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModel
import com.example.shopify.Payment.UI.ViewModel.PaymentViewModelFactory
import com.example.shopify.R
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentPaymentBinding
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {
    lateinit var paymentBinding: FragmentPaymentBinding
    lateinit var viewModel: PaymentViewModel
    lateinit var factory: PaymentViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        paymentBinding = FragmentPaymentBinding.inflate(inflater, container, false)
        return paymentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = PaymentViewModelFactory(PaymentRepository.getInstance(PaymentClient()))
        viewModel = ViewModelProvider(this, factory).get(PaymentViewModel::class.java)

//        val lineItem = LineItem(quantity = 10 , title = "shooes", price = "110" , variant_id = 1234567)
//        val draftOrder = DraftOrdersItem(email = "ahmednoha771@gmail.com" , line_items = listOf(lineItem))
//        val postOrder = PostOrder(draftOrder)

//        viewModel.postOrder(132345555) /// id of cart draft orders
//        lifecycleScope.launch {
//            viewModel.order.collect {
//                when (it) {
//                    is State.Loading -> {
//
//                    }
//                    is State.Success -> {
//                        val orderResponse = it.data
//                        Log.i("TAG", "onViewCreated: $orderResponse")
//
//                    }
//                    else -> {
//
//                    }
//                }
//            }
//
//        }
    }

}