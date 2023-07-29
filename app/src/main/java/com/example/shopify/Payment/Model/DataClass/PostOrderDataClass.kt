package com.example.shopify.Payment.Model.DataClass

data class OrderData(
    val order:Order
)
data class Order(
    val email: String,
    val line_items: List<LineItemm>,
    val discount_codes : List<DiscountCode>,
    val send_receipt: Boolean = true,
    val inventory_behaviour: String = "decrement_obeying_policy",
)
data class LineItemm(
    val quantity: Int,
    val variant_id:Long
)
data class DiscountCode(
    val code: String,
    val amount: String,
    val type:String="percentage"
)