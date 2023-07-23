package com.example.shopify.AllOrdersFragment.UI.View

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.AllOrdersFragment.Model.Order
import com.example.shopify.R
import com.example.shopify.databinding.BrandRawBinding
import com.example.shopify.databinding.OrderRawBinding
import com.example.shopify.utilities.MySharedPreferences
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AllOrdersAdapter(
    private var orderList: List<Order>,
    val context: Context,
    val listner: OnClickOrder,
) : RecyclerView.Adapter<AllOrdersAdapter.ViewHolder>() {

    lateinit var binding: OrderRawBinding

    inner class ViewHolder(var binding: OrderRawBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = OrderRawBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentOrder = orderList[position]
        holder.binding.orderNumValue.text = currentOrder.order_number.toString()
        holder.binding.dateValue.text = convertDateTimeFormat(currentOrder.processed_at!!)
        holder.binding.priceValue.text = formatDecimal(currentOrder.total_price!!.toDouble()* MySharedPreferences
            .getInstance(context).getExchangeRate() )+" "+"${MySharedPreferences.getInstance(context).getCurrencyCode()}"
        holder.binding.orderCardView.setOnClickListener {
            listner.onClickOrder(currentOrder)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

     fun setOrderList(orderList: List<Order>) {
        this.orderList = orderList
        notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTimeFormat(dateTimeString: String): String {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val outputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm")

        val dateTime = LocalDateTime.parse(dateTimeString, inputFormat)
        return dateTime.format(outputFormat)
    }
    fun formatDecimal(decimal: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(decimal)
    }

}