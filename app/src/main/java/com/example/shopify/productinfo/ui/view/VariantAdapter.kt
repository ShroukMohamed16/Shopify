package com.example.shopify.productinfo.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R
import com.example.shopify.databinding.ProductInfoRawBinding
import com.example.shopify.productinfo.model.pojo.Variant


class VariantAdapter(var list:List<Variant>, var listener: OnProductVariantClickListener) : RecyclerView.Adapter<VariantAdapter.VariantViewHolder>() {
    private lateinit var binding: ProductInfoRawBinding
    var selectedItemPosition:Int = RecyclerView.NO_POSITION


    inner class VariantViewHolder(var binding: ProductInfoRawBinding):RecyclerView.ViewHolder(binding.root) {
        fun onBind(currentItem: Variant, position: Int) {
                binding.variantTv.text = currentItem.title

             if (selectedItemPosition == position) {
                    binding.variantCard.setCardBackgroundColor(
                        ContextCompat.getColor(binding.variantCard.context, R.color.primary)
                    )
                } else {
                    binding.variantCard.setCardBackgroundColor(
                        ContextCompat.getColor(binding.variantCard.context, R.color.white)
                    )
                }
            binding.variantCard.setOnClickListener {
                // Save the selected position and update the UI
                listener.onProductVariantClick(currentItem.id!!)
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = position
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)

            }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductInfoRawBinding.inflate(inflater,parent,false)
        return VariantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VariantViewHolder, position: Int) {
       // binding.variantTv.text = list[position].title
        val currentItem = list[position]
        holder.onBind(currentItem,position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setSizeList(values: List<Variant?>?) {
        this.list = values as List<Variant>
        notifyDataSetChanged()
    }



}