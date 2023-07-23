package com.example.shopify.AllOrdersFragment.Remote

import com.example.shopify.AllOrdersFragment.Model.OrderResponse

interface AllOrderRemoteSource {
  suspend  fun getAllOrdersOfUser(email :String ) :OrderResponse
}