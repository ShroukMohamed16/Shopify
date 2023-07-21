package com.example.shopify.productinfo.model.pojo

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class Image {
    @SerializedName("id")
    @Expose
    var id: Long? = null

    @SerializedName("product_id")
    @Expose
    var productId: Long? = null

    @SerializedName("position")
    @Expose
    var position: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("alt")
    @Expose
    var alt: Any? = null

    @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null

    @SerializedName("src")
    @Expose
    var src: String? = null

    @SerializedName("variant_ids")
    @Expose
    var variantIds: List<Int>? = null

    @SerializedName("admin_graphql_api_id")
    @Expose
    var adminGraphqlApiId: String? = null
}