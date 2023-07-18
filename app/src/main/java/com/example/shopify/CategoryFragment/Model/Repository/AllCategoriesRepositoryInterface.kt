package com.example.shopify.CategoryFragment.Model.Repository

import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories

import kotlinx.coroutines.flow.Flow

interface AllCategoriesRepositoryInterface {
    suspend fun getAllCategories(): Flow<Allcategories>
}