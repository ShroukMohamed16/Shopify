package com.example.shopify.CategoryFragment.Model.DataClass

import com.google.gson.annotations.SerializedName


data class Allcategories(var custom_collections: List<CustomCollections>)

data class CustomCollections(
    var id: Long,
    var handle: String,
    var title: String,
    var updated_at: String,
    var body_html: String,
    var published_at: String,
    var sort_order: String,
    var template_suffix: String,
    var published_scope: String,
    var admin_graphql_api_id: String,
    var image: ImageCategory,
)

data class ImageCategory(
    var created_at: String,
    var alt: String,
    var width: Int,
    var height: Int,
    var src: String,
)
