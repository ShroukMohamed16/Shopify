package com.example.shopify.base

import com.example.shopify.authentication.model.pojo.Customer

data class DraftOrderResponse(val draft_order: DraftOrder? = null)
data class DraftOrder(
    var id: Long? = null,
    var order_id: Long? = null,
    var name: String? = null,
    var customer: Customer? = null,
    var shipping_address: shipping_address? = shipping_address(),
    var billing_address: billing_address? = billing_address(),
    var note: String? = null,
    var note_attributes: List<String>? = listOf(),
    var email: String?=null,
    var currency: String? = null,
    var invoice_sent_at: String? = null,
    var invoice_url: String? = null,
    var line_items: List<line_items> = listOf(),
    var shipping_line: shipping_line? = shipping_line(),
    var source_name: String? = null,
    var tags: String? = null,
    var tax_exempt: Boolean? = null,
    var tax_lines: ArrayList<TaxLine> = arrayListOf(),
    var applied_discount: AppliedDiscount? = AppliedDiscount(),
    var taxes_included: Boolean? = null,
    var total_tax: String? = null,
    var subtotal_price: String? = null,
    var total_price: String? = null,
    var completed_at: String? = null,
    var created_at: String? = null,
    var updated_at: String? = null,
    var status: String? = null,
    )

data class shipping_address(
    var address1: String? = null,
    var address2: String? = null,
    var city: String? = null,
    var company: String? = null,
    var country: String? = null,
    var country_code: String? = null,
    var first_name: String? = null,
    var last_name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name: String? = null,
    var phone: String? = null,
    var province: String? = null,
    var province_code: String? = null,
    var zip: String? = null,
)

data class billing_address(
    var first_name: String? = null,
    var address1: String? = null,
    var phone: String? = null,
    var city: String? = null,
    var zip: String? = null,
    var province: String? = null,
    var country: String? = null,
    var last_name: String? = null,
    var address2: String? = null,
    var company: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var name: String? = null,
    var country_code: String? = null,
    var province_code: String? = null,
)

data class line_items(
    var variant_id: Long? = null,
    var product_id: Long? = null,
    var title: String? = null,
    var variant_title: String? = null,
    var sku: String? = null,
    var vendor: String? = null,
    var quantity: Int? = null,
    var requires_shipping: Boolean? = null,
    var taxable: Boolean? = null,
    var gift_card: Boolean? = null,
    var fulfillment_service: String? = null,
    var grams: Int? = null,
    var tax_lines: ArrayList<TaxLine> = arrayListOf(),
    var applied_discount: AppliedDiscount? = null,
    var name: String? = null,
    var properties: ArrayList<Property> = arrayListOf(),
    var custom: Boolean? = null,
    var price: String? = null,
    var admin_graphql_api_id: String? = null,
)

data class TaxLine(
    val price: String,
    val rate: Double,
    val title: String,
)

data class Property(
    var name: String? = "url_image",
    val value: String?,
)

data class AppliedDiscount(

    var description: String? = null,
    var value: String? = null,
    var title: String? = null,
    var amount: String? = null,
    var value_type: String? = null,

    )

data class shipping_line(
    var title: String? = null,
    var custom: Boolean? = null,
    var handle: String? = null,
    var price: String? = null,
)
