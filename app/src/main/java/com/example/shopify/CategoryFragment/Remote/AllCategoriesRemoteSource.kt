package com.example.shopify.CategoryFragment.Remote

import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories

interface AllCategoriesRemoteSource {
    suspend fun getAllCategories(): Allcategories
}