package com.example.shopify.favourite.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.R
import com.example.shopify.base.line_items
import com.example.shopify.databinding.FavouriteItemBinding
import com.example.shopify.utilities.MySharedPreferences

class FavouriteAdapter(var list: List<line_items>,var onClickListener: OnClickListener):RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    lateinit var binding: FavouriteItemBinding
    lateinit var context: Context

    inner class FavouriteViewHolder(var binding: FavouriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        binding = FavouriteItemBinding.inflate(inflater, parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        var currentItem = list[position+1]
        /*Glide.with(holder.itemView)
            .load(currentItem.properties.get(0).name.toString())
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(holder.binding.favProductImg)*/
        holder.binding.favProductName.text = splitTitle(currentItem.title)
        val price=  MySharedPreferences.getInstance(context).getExchangeRate() * currentItem.price!!.toDouble()
        holder.binding.favProductPrice.text = "Price: $price" + MySharedPreferences.getInstance(context).getCurrencyCode()
        holder.binding.favProductDeleteIcon.setOnClickListener{
            onClickListener.onClickDeleteIcon(currentItem)
        }
        holder.binding.favProductCard.setOnClickListener {
            onClickListener.onClickProductCard(currentItem.product_id!!)

        }

    }

    override fun getItemCount(): Int {
        return list.size-1
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setFavList(values: List<line_items>?) {
        this.list = values as List<line_items>
        notifyDataSetChanged()
    }
    fun splitTitle(title:String?):String{
        val titles = title?.split("|")
        return titles!![1]
}


}
