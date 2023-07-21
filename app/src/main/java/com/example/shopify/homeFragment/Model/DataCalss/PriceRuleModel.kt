package com.example.shopify.homeFragment.Model.DataCalss

data class PriceRuleModel(
    val id: Long,
    val value_type: String,
    val value: String,
    val customer_selection: String,
    val target_type: String,
    val target_selection: String,
    val allocation_method: String,
    val allocation_limit: Int?,
    val once_per_customer: Boolean,
    val usage_limit: Int,
    val title: String,
    val admin_graphql_api_id: String
)

data class PriceRules(
    val price_rules: List<PriceRuleModel>
)