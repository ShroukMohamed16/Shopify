package com.example.shopify.productinfo.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ProductInfoRawBinding



class SizeAdapter(var list:List<String>) : RecyclerView.Adapter<SizeAdapter.sizeViewHolder>() {
    private lateinit var binding: ProductInfoRawBinding

    inner class sizeViewHolder(var binding: ProductInfoRawBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sizeViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductInfoRawBinding.inflate(inflater,parent,false)
        return sizeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: sizeViewHolder, position: Int) {
        binding.radioButton.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setSizeList(values: List<String?>?) {
        this.list = values as List<String>
        notifyDataSetChanged()
    }


}