package com.example.shopify.authentication.model.pojo

data class CustomerBodey(var customer: Customerbody)
data class Customerbody(
    val id: Long? = null,
    val email: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val verified_email: Boolean = true,
    val note: String? = "",
    val tags: String? = "",
)