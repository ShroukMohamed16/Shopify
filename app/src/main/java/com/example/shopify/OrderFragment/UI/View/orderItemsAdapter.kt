package com.example.shopify.OrderFragment.UI.View

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.AllOrdersFragment.Model.LineItemsOrder
import com.example.shopify.databinding.OrderItemRawBinding
import com.example.shopify.utilities.MySharedPreferences
import java.text.DecimalFormat

class orderItemsAdapter(
    private var itemOrderList: List<LineItemsOrder>,
    val context: Context,
    var listener: OnClickOrderItem,
) : RecyclerView.Adapter<orderItemsAdapter.ViewHolder>() {

    private lateinit var binding: OrderItemRawBinding

    fun setOrderList(values: List<LineItemsOrder?>?) {
        this.itemOrderList = values as List<LineItemsOrder>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = OrderItemRawBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItemOrder = itemOrderList[position]
       // val result = splitItemOrder(currentItemOrder.title!!)
       // val title = result.first
        holder.binding.orderTitle.text = currentItemOrder.title
        holder.binding.quantityValue.text = currentItemOrder.quantity.toString()
       holder.binding.pricevalue.text = formatDecimal(currentItemOrder.price!!.toDouble()* MySharedPreferences
           .getInstance(context).getExchangeRate() )+" "+"${MySharedPreferences.getInstance(context).getCurrencyCode()}"
//        Glide.with(context)
//            .load(currentItemOrder.properties[0].value)
//            .into(holder.binding.itemImageView)
        holder.binding.itemOrderCardView.setOnClickListener {
            currentItemOrder.product_id?.let { it -> listener.onClickItemOrder(it.toLong()) }
        }
    }
    fun splitItemOrder(string: String): Pair<String, String> {
        val parts = string.split("|")
        val title = parts[0].trim()
        val id = parts[1].trim()
        return Pair(title, id)
    }

    fun formatDecimal(decimal: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(decimal)
    }
    override fun getItemCount(): Int = itemOrderList.size

    inner class ViewHolder(var binding: OrderItemRawBinding) :
        RecyclerView.ViewHolder(binding.root)
}