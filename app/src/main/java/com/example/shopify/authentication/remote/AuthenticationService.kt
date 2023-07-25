package com.example.shopify.authentication.remote

import com.example.shopify.authentication.model.pojo.CustomerBodey
import com.example.shopify.authentication.model.pojo.CustomerListResponse
import com.example.shopify.authentication.model.pojo.CustomerResponse
import com.example.shopify.authentication.model.pojo.Customerbody
import com.example.shopify.base.DraftOrder
import com.example.shopify.base.DraftOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.*

interface AuthenticationService {

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("customers.json")
    suspend fun addNewCustomer(@Body customer: CustomerResponse): CustomerResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @GET("customers.json")
    suspend fun getCustomerByEmail(@Query("email") email:String): CustomerListResponse

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @POST("draft_orders.json")
    suspend fun createDraftOrder( @Body draftOrder: DraftOrderResponse):DraftOrderResponse
    @DELETE
    suspend fun deleteCustomer(customerID: Long)

    @Headers("Content-Type:application/json","X-Shopify-Access-Token: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")
    @PUT("customers/{customer_id}.json")
    suspend fun updateCustomer(@Path(value = "customer_id")customer_id:Long,
                                     @Body customer: Customerbody
    ): CustomerBodey
}