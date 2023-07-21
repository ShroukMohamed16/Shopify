package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EmailMarketingConsent {
    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("opt_in_level")
    @Expose
    var optInLevel: String? = null

    @SerializedName("consent_updated_at")
    @Expose
    var consentUpdatedAt: Any? = null
}