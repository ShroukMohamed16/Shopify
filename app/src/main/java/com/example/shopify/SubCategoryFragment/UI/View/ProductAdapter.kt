package com.example.shopify.SubCategoryFragment.UI.View

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.SubCategoryFragment.Model.Product
import com.example.shopify.databinding.BrandRawBinding
import com.example.shopify.databinding.ProductRawBinding
import com.example.shopify.homeFragment.Model.DataCalss.SmartCollection
import com.example.shopify.utilities.MySharedPreferences
import java.text.DecimalFormat


class ProductAdapter  (private var productList: List<Product>, val context: Context, var listener: OnClickProduct) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    lateinit var binding : ProductRawBinding
    inner class ViewHolder(var binding: ProductRawBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ProductRawBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentProduct= productList[position]
        holder.binding.productTitleTextView.text = currentProduct.title
        holder.binding.productPriceTextView.text =
            formatDecimal(currentProduct.variants[0].price!!.toDouble()*MySharedPreferences
                .getInstance(context).getExchangeRate() )+" "+"${MySharedPreferences.getInstance(context).getCurrencyCode()}"
        Glide.with(context)
            .load(currentProduct.image.src)
            .into(holder.binding.productImageView)
        holder.binding.productCardView.setOnClickListener {
            listener.onClickProduct(currentProduct.id!!)
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
    fun formatDecimal(decimal: Double): String {
        val decimalFormat = DecimalFormat("0.00")
        return decimalFormat.format(decimal)
    }

}