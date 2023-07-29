package com.example.shopify.CartFragment.UI.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.Payment.Model.DataClass.LineItem
import com.example.shopify.R
import com.example.shopify.base.line_items
import com.example.shopify.databinding.CartItemBinding
import com.example.shopify.utilities.MySharedPreferences
import com.example.shopify.utilities.createAlert
import java.text.DecimalFormat


class CartAdapter (
    private var cartList: List<line_items>,
    val context: Context,
    var listener: OnCartClickListener,
) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private lateinit var binding: CartItemBinding

    inner class ViewHolder(var binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CartItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = cartList[position+1]
        var currentQuantity = currentItem.quantity
        val price=  formatDecimal(MySharedPreferences.getInstance(context).getExchangeRate() * currentItem.price!!.toDouble())

        holder.binding.cartItemProductBrand.text= currentItem.name
        holder.binding.cartItemProductCountTv.text=currentQuantity.toString()
        Glide.with(context).load(currentItem.properties[0].value).into(holder.binding.cartItemProductImage)
        holder.binding.cartItemProductProductPrice.text= "${context.getString(R.string.price)} $price ${MySharedPreferences.getInstance(context).getCurrencyCode()}"
        holder.binding.deleteCartItem.setOnClickListener {
            listener.onCartDeleteClickListener(currentItem)
        }
        val quantity = currentItem.properties[1].value?.toInt()
        holder.binding.cartItemProductIncreaseCount.setOnClickListener {
            listener.onCartIncreaseItemClickListener(quantity!!,position+1,currentQuantity!!)
        }
        holder.binding.cartItemProductDecrease.setOnClickListener {
            listener.onCartDecreaseItemClickListener(quantity!!,position+1,currentQuantity!!)
        }

    }

    override fun getItemCount(): Int {
        return cartList.size-1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(values: List<line_items>) {
        this.cartList = values
        notifyDataSetChanged()
    }

}

fun formatDecimal(decimal: Double): String {
    val decimalFormat = DecimalFormat("0.00")
    return decimalFormat.format(decimal)
}