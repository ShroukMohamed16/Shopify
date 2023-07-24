package com.example.shopify.search.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.SubCategoryFragment.Model.Product
import com.example.shopify.databinding.SearchItemBinding

class SearchAdapter (private var productList: List<Product>, val context: Context,var listener:OnSearchItemClick) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private lateinit var binding: SearchItemBinding
    inner class ViewHolder(var binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = SearchItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.binding.searchProductName.text = currentProduct.title
        Glide.with(context)
            .load(currentProduct.images.get(0).src)
            .into(holder.binding.searchProductImg)
        holder.binding.searchCardResult.setOnClickListener {
            listener.onProductClick(currentProduct.id!!)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(values: List<Product?>?) {
        this.productList = values as List<Product>
        notifyDataSetChanged()
    }
}