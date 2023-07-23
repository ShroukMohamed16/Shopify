package com.example.shopify.OrderFragment.UI.View

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopify.AllOrdersFragment.UI.View.AllOrdersFragmentDirections
import com.example.shopify.R
import com.example.shopify.databinding.FragmentOrderBinding
import com.example.shopify.utilities.MySharedPreferences
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val order = args.order
        Log.i("TAG", "onViewCreated: $order")
        orderBinding.nameValue.text = order.order_number.toString()
        orderBinding.orderNumValue.text = order.processed_at?.let { convertDateTimeFormat(it) }
        orderBinding.priceValue.text = formatDecimal(order.current_subtotal_price!!.toDouble()* MySharedPreferences
            .getInstance(requireContext()).getExchangeRate() )+" "+"${MySharedPreferences.getInstance(requireContext()).getCurrencyCode()}"

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
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTimeFormat(dateTimeString: String): String {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")

        val dateTime = LocalDateTime.parse(dateTimeString, inputFormat)
        return dateTime.format(outputFormat)
    }

    override fun onClickItemOrder(id: Long) {
        val action = OrderFragmentDirections.actionOrderFragmentToProductInfoFragment2(id.toString())
        orderBinding.root.findNavController().navigate(action)
    }
    fun formatDecimal(decimal: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(decimal)
    }
}