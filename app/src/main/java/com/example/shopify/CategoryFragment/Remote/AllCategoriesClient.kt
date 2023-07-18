package com.example.shopify.CategoryFragment.Remote

import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories
import com.example.shopify.base.Remote.RetrofitHelper
import com.example.shopify.homeFragment.Remote.BrandsService

class AllCategoriesClient : AllCategoriesRemoteSource {
    val categoryService: AllCategoriesService by lazy {
        RetrofitHelper.retrofitInstance.create(AllCategoriesService::class.java)
    }

    override suspend fun getAllCategories(): Allcategories {
        return categoryService.getAllCategories()
    }
}