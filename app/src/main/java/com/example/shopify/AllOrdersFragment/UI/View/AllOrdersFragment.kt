package com.example.shopify.AllOrdersFragment.UI.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.AllOrdersFragment.Model.OrderRepository.AllOrderRepository
import com.example.shopify.AllOrdersFragment.Remote.AllOrderClinet
import com.example.shopify.AllOrdersFragment.UI.ViewModel.ALlOrderViewModel
import com.example.shopify.AllOrdersFragment.UI.ViewModel.AllOrderViewModelFactory
import com.example.shopify.base.State
import com.example.shopify.databinding.FragmentAllOrdersBinding
import com.example.shopify.utilities.MySharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AllOrdersFragment : Fragment(), OnClickOrder {
    lateinit var allOrderBinding: FragmentAllOrdersBinding
    lateinit var orderAdapter: AllOrdersAdapter
    lateinit var viewModel: ALlOrderViewModel
    lateinit var factory: AllOrderViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        allOrderBinding = FragmentAllOrdersBinding.inflate(inflater, container, false)
        return allOrderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = AllOrderViewModelFactory(AllOrderRepository.getInstance(AllOrderClinet()))
        viewModel = ViewModelProvider(this, factory).get(ALlOrderViewModel::class.java)
        orderAdapter = AllOrdersAdapter(ArrayList(), requireContext(), this)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val customerEmail = MySharedPreferences.getInstance(requireContext()).getCustomerEmail()
        viewModel.getAllOrdersOfUser(customerEmail!!)
        lifecycleScope.launch {
            viewModel.order.collect { result ->
                when (result) {
                    is State.Loading -> {
                        Log.i("TAG", "onViewCreated: loading the order List")
                        allOrderBinding.progressBar.visibility = View.VISIBLE
                        allOrderBinding.ordersRecyclerView.visibility = View.GONE
                    }


                    is State.Success -> {
                        Log.i("TAG", "onViewCreated: success the order List")
                        val orderList = result.data.orders
                        if (orderList.isNullOrEmpty()) {
                            allOrderBinding.noOrderAnmi.visibility =View.VISIBLE
                            allOrderBinding.noOrdersTxt.visibility = View.VISIBLE
                            allOrderBinding.listScroll.visibility = View.GONE
                            Log.i("TAG", "onViewCreated: null listssssssssss")
                        } else {
                            Log.i("TAG", "onViewCreated: not null listssssssssss")
                            allOrderBinding.progressBar.visibility = View.GONE
                            allOrderBinding.noOrderAnmi.visibility =View.GONE
                            allOrderBinding.noOrdersTxt.visibility = View.GONE
                            allOrderBinding.ordersRecyclerView.visibility = View.VISIBLE
                            allOrderBinding.ordersRecyclerView.layoutManager = layoutManager
                            orderAdapter.setOrderList(orderList)
                            allOrderBinding.ordersRecyclerView.adapter = orderAdapter
                        }
                    }
                    else -> {
                        Log.i("TAG", "onViewCreated: ")

                    }

                }
            }
        }


    }

    override fun onClickOrder(order: Order) {
        val action = AllOrdersFragmentDirections.actionAllOrdersFragmentToOrderFragment(order)
        allOrderBinding.root.findNavController().navigate(action)
    }

}