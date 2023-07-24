package com.example.shopify.search.model.pojo

import com.example.shopify.SubCategoryFragment.Model.Product
import com.google.gson.annotations.SerializedName

class ProductListResponse {

    @SerializedName("products")
    private var products: List<Product> = listOf()

    fun getProducts(): List<Product>{
        return products
    }

    fun setProducts(products: List<Product>) {
        this.products = products
    }
}