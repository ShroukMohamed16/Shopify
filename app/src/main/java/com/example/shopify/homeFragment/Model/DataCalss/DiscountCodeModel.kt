package com.example.shopify.homeFragment.Model.DataCalss

data class DiscountCodeModel(
    val id: Long,
    val price_rule_id: Long,
    val code: String,
    val usage_count: Int,
    val created_at: String,
    val updated_at: String
)

data class DiscountCodes(
    val discount_codes: List<DiscountCodeModel>
)

