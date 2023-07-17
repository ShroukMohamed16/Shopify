package com.example.shopify.homeFragment.UI.View

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.databinding.BrandRawBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection

class BrandAdapter (private var brandList: List<SmartCollection>, val context: Context, var listener: OnBrandClick) :
    RecyclerView.Adapter<BrandAdapter.ViewHolder>() {
    private lateinit var binding: BrandRawBinding

    inner class ViewHolder(var binding: BrandRawBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = BrandRawBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBrand = brandList[position]
        holder.binding.brandTitle.text = currentBrand.title
        Glide.with(context)
            .load(currentBrand.image?.src)
            .into(holder.binding.brandImage)
        holder.binding.brandCardView.setOnClickListener {
            listener.onBrandClick(currentBrand.title!!)
        }
    }

    override fun getItemCount(): Int {
        return brandList.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setBrandList(values: List<SmartCollection?>?) {
        this.brandList = values as List<SmartCollection>
        notifyDataSetChanged()
    }
}