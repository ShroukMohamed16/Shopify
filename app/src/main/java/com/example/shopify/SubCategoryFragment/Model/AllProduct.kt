package com.example.shopify.SubCategoryFragment.Model

data class AllProduct(var products : List<Product>)

data class Product(
    var id: Long? = 0,
    var title: String? = "",
    var body_html: String? = "",
    var vendor: String? = "",
    var product_type: String? = "",
    var created_at: String? = "",
    var handle: String? = "",
    var updated_at: String? = "",
    var published_at: String? = "",
    var template_suffix: String? = "",
    var status: String? = "",
    var published_scope: String? = "",
    var tags: String? = "",
    var admin_graphql_api_id: String? = "",
    var variants: List<Variants>,
    var options: List<Options>,
    var images: List<Image>,
    var image: Image,
)

data class Variants(
    var id: Long? = 0,
    var product_id: Long? = 0,
    var title: String? = "",
    var price: String? = "",
    var sku: String? = "",
    var position: Int? = 0,
    var inventory_policy: String? = "",
    var compare_at_price: String? = "",
    var fulfillment_service: String? = "",
    var inventory_management: String? = "",
    var option1: String? = "",
    var option2: String? = "",
    var option3: String? = "",
    var created_at: String? = "",
    var updated_at: String? = "",
    var taxable: Boolean? = false,
    var barcode: String? = "",
    var grams: Int? = 0,
    var tracked: Boolean = true,
    var image_id: String? = "",
    var weight: Int? = 0,
    var weight_unit: String? = "",
    var inventory_item_id: Long? = 0,
    var inventory_quantity: Int? = 0,
    var old_inventory_quantity: Int? = 0,
    var requires_shipping: Boolean? = false,
    var admin_graphql_api_id: String? = "",
)

data class Options(
    var id: Long? = 0,
    var product_id: Long? = 0,
    var name: String? = "",
    var position: Int? = 0,
)

data class Image(
    var id: Long? = 0,
    var product_id: Long? = 0,
    var position: Int? = 0,
    var created_at: String? = "",
    var updated_at: String? = "",
    var alt: String? = "",
    var width: Int? = 0,
    var height: Int? = 0,
    var src: String? = "",
    var variant_ids: List<String>,
    var admin_graphql_api_id: String? = "",
)
