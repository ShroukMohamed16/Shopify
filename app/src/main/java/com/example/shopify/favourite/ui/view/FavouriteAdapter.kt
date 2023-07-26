package com.example.shopify.favourite.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FavouriteItemBinding

class FavouriteAdapter(var list: List<line_items>):RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>(){

    lateinit var binding: FavouriteItemBinding

    inner class FavouriteViewHolder(var binding:FavouriteItemBinding):RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FavouriteItemBinding.inflate(inflater,parent,false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var currentItem = list[position]
        holder.binding.favProductName.text = currentItem.name
        holder.binding.favProductPrice.text = currentItem.price
        holder.binding.favProductDeleteIcon.setOnClickListener {

        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFavList(values: List<line_items>?) {
        this.list = values as List<line_items>
        notifyDataSetChanged()
    }


}