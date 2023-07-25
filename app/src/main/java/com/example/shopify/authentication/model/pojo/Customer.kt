package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Customer(var customerEmail:String?,var customerFirstName:String?,var customerLastName:String?){
    @SerializedName("id")
    @Expose
    var id: Long? = null

    @SerializedName("email")
    @Expose
    var email: String? = customerEmail

    @SerializedName("accepts_marketing")
    @Expose
    var acceptsMarketing: Boolean? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = customerFirstName

    @SerializedName("last_name")
    @Expose
    var lastName: String? = customerLastName

    @SerializedName("orders_count")
    @Expose
    var ordersCount: Int? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("total_spent")
    @Expose
    var totalSpent: String? = null

    @SerializedName("last_order_id")
    @Expose
    var lastOrderId: Any? = null

    @SerializedName("note")
    @Expose
    var note: Long? = null

    @SerializedName("verified_email")
    @Expose
    var verifiedEmail: Boolean? = null

    @SerializedName("multipass_identifier")
    @Expose
    var multipassIdentifier: Long? = null

    @SerializedName("tax_exempt")
    @Expose
    var taxExempt: Boolean? = null

    @SerializedName("tags")
    @Expose
    var tags: String? = null

    @SerializedName("last_order_name")
    @Expose
    var lastOrderName: Any? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("addresses")
    @Expose
    var addresses: List<Address>? = null

    @SerializedName("accepts_marketing_updated_at")
    @Expose
    var acceptsMarketingUpdatedAt: String? = null

    @SerializedName("marketing_opt_in_level")
    @Expose
    var marketingOptInLevel: Any? = null

    @SerializedName("tax_exemptions")
    @Expose
    var taxExemptions: List<Any>? = null

    @SerializedName("email_marketing_consent")
    @Expose
    var emailMarketingConsent: EmailMarketingConsent? = null

    @SerializedName("sms_marketing_consent")
    @Expose
    var smsMarketingConsent: SmsMarketingConsent? = null

    @SerializedName("admin_graphql_api_id")
    @Expose
    var adminGraphqlApiId: String? = null

    @SerializedName("default_address")
    @Expose
    var defaultAddress: DefaultAddress? = null
}