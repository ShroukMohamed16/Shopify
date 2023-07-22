package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface AuthenticationService {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("customers.json")
    suspend fun addNewCustomer(@Body customer: CustomerResponse): CustomerResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("customers.json")
    suspend fun getCustomerByEmail(@Query("email") email:String): CustomerListResponse


    @DELETE
    suspend fun deleteCustomer(customerID: Long)
}