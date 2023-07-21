package com.example.shopify.homeFragment.UI.View

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopify.R

class AdsAdapter(private val images: List<Int>, var listener: OnAdsClickListener) :
    RecyclerView.Adapter<AdsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ads_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
        holder.itemView.setOnClickListener {
            listener.onAdsClick(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: Int) {
            itemView.findViewById<ImageView>(R.id.adsImageView).setImageResource(image)
        }
    }

}

interface OnAdsClickListener {
    fun onAdsClick(view: View, position: Int)
}