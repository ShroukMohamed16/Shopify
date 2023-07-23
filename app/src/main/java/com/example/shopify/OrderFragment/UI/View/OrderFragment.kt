package com.example.shopify.OrderFragment.UI.View

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.AllOrdersFragment.UI.View.AllOrdersFragmentDirections
import com.example.shopify.R
import com.example.shopify.databinding.FragmentOrderBinding

class OrderFragment : Fragment(),OnClickOrderItem {
    lateinit var orderBinding: FragmentOrderBinding
    val args: OrderFragmentArgs by navArgs()
    lateinit var orderItemsAdapter: orderItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        orderBinding = FragmentOrderBinding.inflate(inflater, container, false)
        return orderBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order = args.order
        Log.i("TAG", "onViewCreated: $order")
        orderBinding.nameValue.text = order.name
        orderBinding.orderNumValue.text = order.order_number.toString()
        orderBinding.priceValue.text = order.current_subtotal_price

        orderItemsAdapter = orderItemsAdapter(ArrayList(), requireActivity(), this)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        orderBinding.orderItemRV.layoutManager = layoutManager

        orderItemsAdapter.setOrderList(order.line_items)
        orderBinding.orderItemRV.adapter = orderItemsAdapter

        orderBinding.backImg.setOnClickListener {
            val action = OrderFragmentDirections.actionOrderFragmentToAllOrdersFragment()
            orderBinding.root.findNavController().navigate(action)
        }


    }

    override fun onClickItemOrder(id: Long) {
        val action = OrderFragmentDirections.actionOrderFragmentToProductInfoFragment2(id.toString())
        orderBinding.root.findNavController().navigate(action)
    }

}