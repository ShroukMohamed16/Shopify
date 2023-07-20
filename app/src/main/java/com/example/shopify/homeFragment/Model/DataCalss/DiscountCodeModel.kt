package com.example.shopify.homeFragment.Model.DataCalss

data class DiscountCodeModel(
    val id: Int,
    val price_rule_id: Int,
    val code: String,
    val usage_count: Int,
    val created_at: String,
    val updated_at: String
)


