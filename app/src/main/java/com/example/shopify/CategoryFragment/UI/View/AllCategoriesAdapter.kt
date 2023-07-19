package com.example.shopify.CategoryFragment.UI.View

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopify.CategoryFragment.Model.DataClass.CustomCollections
import com.example.shopify.databinding.BrandRawBinding
import com.example.shopify.databinding.CategoryRawBinding

class AllCategoriesAdapter(
    private var categoryList: List<CustomCollections>,
    val context: Context,
    var listener: OnClickCategory,
) :
    RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder>() {
    private lateinit var binding: CategoryRawBinding

    inner class ViewHolder(var binding: CategoryRawBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CategoryRawBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val currentCategory = categoryList[position+1]
        holder.binding.categoryTitle.text=currentCategory.title
        holder.binding.categoryImage.clipToOutline = true
        Glide.with(context)
            .load(currentCategory.image?.src)
            .into(holder.binding.categoryImage)
        holder.binding.categoryCardView.setOnClickListener {
            listener.onCategoryClick(currentCategory.id)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size-1
    }
    fun setCategoryList(values: List<CustomCollections?>?) {
        this.categoryList = values as List<CustomCollections>
    }
}