package com.example.shopify.address.remote


import com.example.shopify.address.model.AddressBody
import com.example.shopify.address.model.AddressResponse
import com.example.shopify.address.model.GetAddressResponse
import retrofit2.Response
import retrofit2.http.*

interface AddressService {
    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("customers/{id}/addresses.json")
    suspend fun getCustomerAddress(@Path("id") id: Long): GetAddressResponse?

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("customers/{customer_id}/addresses.json")
    suspend fun addCustomerAddress(@Path(value="customer_id") id:Long ,@Body address: AddressBody) :Response<AddressResponse>

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @DELETE("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun deleteCustomerAddress(@Path(value="customer_id") customer_id:Long ,@Path (value="address_id") address_id:Long)


}