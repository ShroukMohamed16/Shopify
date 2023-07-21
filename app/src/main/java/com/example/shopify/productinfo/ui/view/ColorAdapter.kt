package com.example.shopify.productinfo.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.databinding.ProductInfoRawBinding
import com.example.shopify.productinfo.model.pojo.Option

class ColorAdapter(var list:List<String>) : RecyclerView.Adapter<ColorAdapter.colorViewHolder>() {
    private lateinit var binding: ProductInfoRawBinding

    inner class colorViewHolder(var binding: ProductInfoRawBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): colorViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductInfoRawBinding.inflate(inflater, parent, false)
        return colorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: colorViewHolder, position: Int) {
        binding.radioButton.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setColorList(values: List<String?>?) {
        this.list = values as List<String>
        notifyDataSetChanged()
    }
}
