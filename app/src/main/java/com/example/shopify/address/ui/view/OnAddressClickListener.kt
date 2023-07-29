package com.example.shopify.address.ui.view


interface OnAddressClickListener {
    fun onAddressDeleteListener(id:Long,address_id:Long)
    fun onAddressCardDeleteListener(address:com.example.shopify.address.model.Address)
}
