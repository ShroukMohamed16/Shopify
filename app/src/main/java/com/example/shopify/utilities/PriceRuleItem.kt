package com.example.shopify.utilities

object MyPriceRules {
    private val priceRules = mutableListOf<PriceRuleItem>()
    var rules: List<PriceRuleItem> = priceRules

    fun addPriceRule(id: Long, title: String) {
        priceRules.add(PriceRuleItem(id, title))
        rules = priceRules
    }
}

data class PriceRuleItem(
    val id: Long,
    val title: String
)