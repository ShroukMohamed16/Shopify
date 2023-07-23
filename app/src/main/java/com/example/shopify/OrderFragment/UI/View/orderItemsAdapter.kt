package com.example.shopify.OrderFragment.UI.View

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.AllOrdersFragment.Model.LineItemsOrder
import com.example.shopify.databinding.OrderItemRawBinding

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItemOrder = itemOrderList[position]
        val result = splitItemOrder(currentItemOrder.title!!)
        val title = result.first
        holder.binding.orderTitle.text = title
        holder.binding.quantityValue.text = currentItemOrder.quantity.toString()
       holder.binding.pricevalue.text = currentItemOrder.price
//        Glide.with(context)
//            .load(currentItemOrder.)
//            .into(holder.binding.itemImageView)
        holder.binding.itemOrderCardView.setOnClickListener {
            currentItemOrder.product_id?.let { it1 -> listener.onClickItemOrder(it1.toLong()) }
        }
    }
    fun splitItemOrder(string: String): Pair<String, String> {
        val parts = string.split("|")
        val title = parts[0].trim()
        val id = parts[1].trim()
        return Pair(title, id)
    }
    override fun getItemCount(): Int = itemOrderList.size

    inner class ViewHolder(var binding: OrderItemRawBinding) :
        RecyclerView.ViewHolder(binding.root)
}