package com.example.shopify.homeFragment.Model.DataCalss

data class AllBrandsModel(
    val smart_collections :List<SmartCollection>
)

data class SmartCollection(
    val admin_graphql_api_id: String?=null,
    val body_html: String?=null,
    val handle: String? = null,
    val id: Long?=null,
    val image: Image?=null,
    val published_at: String?=null,
    val published_scope: String?=null,
    val rules: List<Rule>?=null,
    val disjunctive: Boolean?=null,
    val sort_order: String?=null,
    val template_suffix: String?=null,
    val title: String?=null,
    val updated_at: String?=null,
)

data class Image(
    val alt: String,
    val created_at: String,
    val src: String,
    val height: Int,
    val width: Int

)

data class Rule(
    val column: String?=null,
    val relation: String?=null,
    val condition: Any?=null,
    val condition_object_id: String? = null
)
