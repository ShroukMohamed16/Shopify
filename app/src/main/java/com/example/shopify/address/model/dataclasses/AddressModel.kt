package com.example.shopify.address.model

data class Address(
    val customer_id:Long,
    val address1: String?,
    val address2: String?,
    val city: String?,
    val company: String?,
    val first_name: String?,
    val last_name: String?,
    val phone: String?,
    val province: String?,
    val country: String?,
    val zip: String?,
    val name: String?,
    val province_code: String?,
    val country_code: String?,
    val country_name: String?,
    val id:Long = 0
)

data class AddressResponse(
    val address: List<Address>
)