package com.example.shopify.CartFragment.UI.View

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.Payment.Model.DataClass.LineItem
import com.example.shopify.databinding.CartItemBinding


class CartAdapter (
    private var cartList: List<LineItem>,
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
        val currentItem = cartList.get(position)

        holder.binding.cartItemProductBrand.text= currentItem.name
        holder.binding.cartItemProductCountTv.text=currentItem.quantity.toString()

    }

    override fun getItemCount(): Int {
        return cartList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setAddressList(values: List<LineItem>) {
        this.cartList = values
        notifyDataSetChanged()
    }

}