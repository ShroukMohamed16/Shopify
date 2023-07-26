package com.example.shopify.authentication.model.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CustomerResponsePut(var customer: CustomerBody)
data class CustomerBody(
   val id :Long?= null,
    val note: String? = "",
    val tags: String? = "",
)



class CustomerRes(
    @SerializedName("id")
    @Expose
    var id: Long? = null,

    @SerializedName("email")
    @Expose
    var email: String? = "",

    @SerializedName("accepts_marketing")
    @Expose
    var acceptsMarketing: Boolean? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("first_name")
    @Expose
    var firstName: String? ="",

    @SerializedName("last_name")
    @Expose
    var lastName: String? = "",

    @SerializedName("orders_count")
    @Expose
    var ordersCount: Int? = null,

    @SerializedName("state")
    @Expose
    var state: String? = null,

    @SerializedName("total_spent")
    @Expose
    var totalSpent: String? = null,

    @SerializedName("last_order_id")
    @Expose
    var lastOrderId: Any? = null,

    @SerializedName("note")
    @Expose
    var note: String? = null,

    @SerializedName("verified_email")
    @Expose
    var verifiedEmail: Boolean? = null,

    @SerializedName("multipass_identifier")
    @Expose
    var multipassIdentifier: Long? = null,

    @SerializedName("tax_exempt")
    @Expose
    var taxExempt: Boolean? = null,

    @SerializedName("tags")
    @Expose
    var tags: String? = null,

    @SerializedName("last_order_name")
    @Expose
    var lastOrderName: Any? = null,

    @SerializedName("currency")
    @Expose
    var currency: String? = null,

    @SerializedName("phone")
    @Expose
    var phone: String? = null,

    @SerializedName("addresses")
    @Expose
    var addresses: List<Address>? = null,

    @SerializedName("accepts_marketing_updated_at")
    @Expose
    var acceptsMarketingUpdatedAt: String? = null,

    @SerializedName("marketing_opt_in_level")
    @Expose
    var marketingOptInLevel: Any? = null,

    @SerializedName("tax_exemptions")
    @Expose
    var taxExemptions: List<Any>? = null,

    @SerializedName("email_marketing_consent")
    @Expose
    var emailMarketingConsent: EmailMarketingConsent? = null,

    @SerializedName("sms_marketing_consent")
    @Expose
    var smsMarketingConsent: SmsMarketingConsent? = null,

    @SerializedName("admin_graphql_api_id")
    @Expose
    var adminGraphqlApiId: String? = null,

    @SerializedName("default_address")
    @Expose
    var defaultAddress: DefaultAddress? = null
)

//
//data class CustomerResponsePut(var customer: Customer)
//
//data class CustomerOne(
//    var id: Long?=null,
//    var email: String?=null,
//    val email_marketing_consent: EmailMarketingConsent?=null,
//    var accepts_marketing: Boolean?=null,
//    var created_at: String?=null,
//    var updated_at: String?=null,
//    var first_name: String?=null,
//    var last_name: String?=null,
//    var orders_count: Int?=null,
//    var state: String?=null,
//    var total_spent: String?=null,
//    var last_order_id: Long?=null,
//    var note: String?=null,
//    var verified_email: Boolean?=null,
//    var multipass_identifier: String?=null,
//    var tax_exempt: Boolean?=null,
//    var phone: String?=null,
//    var tags: String?=null,
//    var last_order_name: String?=null,
//    var  currency: String?=null,
//    var addresses: List<Address>?=null,
//    var accepts_marketing_updated_at: String?=null,
//    val sms_marketing_consent: SmsMarketingConsent?=null,
//    var marketing_opt_in_level: Any?=null,
//    var tax_exemptions: List<Any>?=null,
//    var admin_graphql_api_id: String?=null,
//    var default_address: Addresses?=null
//)
//
//data class Addresses(
//    var id: Long?=null,
//    var customer_id: Long?=null,
//    var first_name: String?=null,
//    var last_name: String?=null,
//    var company: String?=null,
//    var address1: String?=null,
//    var address2: String?=null,
//    var city: String?=null,
//    var province: String?=null,
//    var country: String?=null,
//    var zip: String?=null,
//    var phone: String?=null,
//    var name: String?=null,
//    var province_code: String?=null,
//    var country_code: String?=null,
//    var country_name: String?=null,
//    var default: Boolean?=null
//)