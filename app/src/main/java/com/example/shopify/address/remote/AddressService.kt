package com.example.shopify.address.remote

import com.example.shopify.address.model.Address
import com.example.shopify.address.model.AddressResponse
import retrofit2.http.*

interface AddressService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("customers/{id}/addresses.json")
    suspend fun getCustomerAddress(@Path("id") id: Long):AddressResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("customers/{customer_id}/addresses.json")
    suspend fun addCustomerAddress(@Path(value="customer_id") id:String ,@Body address: Address) :AddressResponse
}