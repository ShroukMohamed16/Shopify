package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DefaultAddress {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("customer_id")
    @Expose
    var customerId: Int? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null

    @SerializedName("company")
    @Expose
    var company: Any? = null

    @SerializedName("address1")
    @Expose
    var address1: String? = null

    @SerializedName("address2")
    @Expose
    var address2: Any? = null

    @SerializedName("city")
    @Expose
    var city: String? = null

    @SerializedName("province")
    @Expose
    var province: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("zip")
    @Expose
    var zip: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("province_code")
    @Expose
    var provinceCode: String? = null

    @SerializedName("country_code")
    @Expose
    var countryCode: String? = null

    @SerializedName("country_name")
    @Expose
    var countryName: String? = null

    @SerializedName("default")
    @Expose
    var default: Boolean? = null
}