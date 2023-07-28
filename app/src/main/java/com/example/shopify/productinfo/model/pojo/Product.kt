package com.example.shopify.productinfo.model.pojo

import com.example.shopify.Payment.Model.DataClass.AppliedDiscount
import com.example.shopify.Payment.Model.DataClass.LineItem
import com.example.shopify.base.line_items
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Product(
    @SerializedName("id") var id: Long? = 0,
    @SerializedName("title") var title: String? = "",
    @SerializedName("body_html") var bodyHtml: String? = "",
    @SerializedName("vendor") var vendor: String? = "",
    @SerializedName("product_type") var productType: String? = "",
    @SerializedName("created_at") var createdAt: String? = "",
    @SerializedName("handle") var handle: String? = "",
    @SerializedName("updated_at") var updatedAt: String? = "",
    @SerializedName("published_at") var publishedAt: String? = "",
    @SerializedName("template_suffix") var templateSuffix: String? = "",
    @SerializedName("status") var status: String? = "",
    @SerializedName("published_scope") var publishedScope: String? = "",
    @SerializedName("tags") var tags: String? = "",
    @SerializedName("admin_graphql_api_id") var adminGraphqlApiId: String? = "",
    @SerializedName("variants") var variants: List<Variant>,
    @SerializedName("options") var options: List<Option>,
    @SerializedName("images") var images: List<Image>,
    @SerializedName("image") var image: Image
)


