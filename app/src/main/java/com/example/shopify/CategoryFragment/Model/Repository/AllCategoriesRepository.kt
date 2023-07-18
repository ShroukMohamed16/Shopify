package com.example.shopify.CategoryFragment.Model.Repository

import com.example.shopify.CategoryFragment.Model.DataClass.Allcategories
import com.example.shopify.CategoryFragment.Remote.AllCategoriesRemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AllCategoriesRepository (var categoryRemote : AllCategoriesRemoteSource) : AllCategoriesRepositoryInterface {

    companion object {
        private var instance: AllCategoriesRepository? = null
        fun getInstance(remote: AllCategoriesRemoteSource): AllCategoriesRepository {
            return instance ?: synchronized(this) {
                val Instance = AllCategoriesRepository( remote)
                instance = Instance
                Instance
            }
        }
    }

    override suspend fun getAllCategories(): Flow<Allcategories> {
        return flowOf(categoryRemote.getAllCategories())
    }
}