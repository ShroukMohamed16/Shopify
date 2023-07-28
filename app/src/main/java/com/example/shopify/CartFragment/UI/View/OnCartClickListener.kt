package com.example.shopify.CartFragment.UI.View

import com.example.shopify.base.line_items

interface OnCartClickListener {

    fun onCartDeleteClickListener(lineItem: line_items)

    fun onCartIncreaseItemClickListener(inventoryQuantity:Int,position:Int,currentQuantity:Int)

    fun onCartDecreaseItemClickListener(inventoryQuantity:Int,position:Int,currentQuantity:Int)
}